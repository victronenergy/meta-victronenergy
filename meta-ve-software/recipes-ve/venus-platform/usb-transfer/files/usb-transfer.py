import xml.etree.ElementTree as ET
from lxml import etree
import re
import argparse
import sys
import os
import subprocess


# TODO
# Restore only enabled tanks and check if that tanks are available in the restoring system

# 2025.05.05
VERSION = 0.1

if os.path.isfile("/opt/victronenergy/version"):
    print("Running on Venus OS")
    SETTINGS_FILE = "/data/conf/settings.xml"
    BACKUP_FILE = "/media/sda1/tank-setups-export.xml"
else:
    print("Running on Linux")
    # SETTINGS_FILE = "./settings.xml"
    SETTINGS_FILE = "./settings-einstein.xml"
    # SETTINGS_FILE = "./settings-ekrano.xml"
    BACKUP_FILE = "./tank-setups-export.xml"

# Define the device patterns to match
PATTERNS = [
    "adc_builtin_tank_",
    "adc_builtin0_",
    "adc_gxtank_",
    # "virtual_",  # there are no settings yet for virtual tanks
]

# Regex to extract serial numbers (e.g., HQ2229T96N3 from adc_gxtank_HQ2229T96N3_0)
SERIAL_REGEX = re.compile(r"(HQ[A-Z0-9]{9,})")


def copy_element(source, target):
    """
    Recursively copy all sub-elements, attributes, and text from the source element
    to the target element.
    """
    # Copy attributes
    target.attrib = source.attrib

    # Copy text
    target.text = source.text

    # Recursively copy sub-elements
    for child in source:
        new_child = ET.SubElement(target, child.tag)
        copy_element(child, new_child)


def clean_xml(element):
    """
    Recursively clean up the XML tree by stripping unnecessary whitespace
    from text and tail attributes.
    """
    if element.text:
        element.text = element.text.strip()
    if element.tail:
        element.tail = element.tail.strip()
    for child in element:
        clean_xml(child)


def sort_xml(element):
    """
    Recursively sort the XML tree by element tag and attributes.
    """
    # Sort child elements by tag and attributes
    element[:] = sorted(element, key=lambda e: (e.tag, sorted(e.attrib.items())))
    # Recursively sort each child
    for child in element:
        sort_xml(child)


def prettify_xml(element):
    """
    Return a pretty-printed XML string for the given ElementTree element,
    including an XML declaration with UTF-8 encoding.
    """
    # Clean up the XML tree
    clean_xml(element)

    # Convert the ElementTree element to a string
    rough_string = ET.tostring(element, encoding="UTF-8", method="xml")
    # Parse the string with lxml and pretty-print it
    parsed = etree.fromstring(rough_string)
    pretty_xml = etree.tostring(parsed, pretty_print=True, encoding="UTF-8", xml_declaration=True).decode("UTF-8")
    return pretty_xml


def write_to_dbus(path, value):
    """
    Write a value to the specified D-Bus path and capture the output.
    """
    command = f'dbus -y com.victronenergy.settings {path} SetValue "{value}"'
    result = subprocess.run(command, shell=True, capture_output=True, text=True)

    if result.returncode != 0:
        print(f"ERROR: Writing to D-Bus: {result.stderr}")

    return result.returncode


def create_notification_gui(title, message, severity=2):
    """
    Create a notification GUI with the given message.
    0 = Error
    1 = Warning
    2 = Info
    """
    os.system(f'dbus -y com.victronenergy.platform /Notifications/Inject SetValue "{severity}	{title}	{message}"')


def export_setups():
    try:
        print(f"Backing up configuration from {SETTINGS_FILE} to {BACKUP_FILE}")

        # Load the XML file
        settings_tree = ET.parse(SETTINGS_FILE)

        # Get the root element of the XML tree
        settings_root = settings_tree.getroot()

        # Recursively sort the XML tree
        # This is needed to make sure, that devices with serial numbers are sorted
        sort_xml(settings_root)

        # Create a new XML tree for the output and copy the attributes from the root
        backup_root = ET.Element("Settings", attrib=settings_root.attrib)

        # Add a Devices sub-element
        backup_devices_element = ET.SubElement(backup_root, "Devices")

        # Initialize serial number replacement logic
        serial_number_map = {}
        next_serial_number = 1

        # Traverse the XML tree and extract matching subpaths
        for elem in settings_root.findall(".//Devices/*"):
            for pattern in PATTERNS:
                if elem.tag.startswith(pattern):
                    print(f"Backup device {elem.tag} (matches {pattern})")

                    # Extract the serial number from the tag
                    match = SERIAL_REGEX.search(elem.tag)
                    if match:
                        original_serial = match.group(1)
                        if original_serial not in serial_number_map:
                            # Generate a new serial number
                            new_serial = f"HQ{next_serial_number:09d}"
                            serial_number_map[original_serial] = new_serial
                            next_serial_number += 1

                        # Replace the serial number in the tag
                        new_tag = elem.tag.replace(original_serial, serial_number_map[original_serial])
                        print(f"|- Updated tag: {elem.tag} -> {new_tag}")

                    # No serial number found, keep the tag as is
                    else:
                        # check if the tag contains builtin0 and replace it with builtin_tank
                        # TODO does not work
                        if "builtin0" in elem.tag:
                            new_tag = elem.tag.replace("builtin0", "builtin_tank")
                            print(f"|- Updated tag: {elem.tag} -> {new_tag}")
                        else:
                            new_tag = elem.tag

                    # Add the matching element to the Devices sub-element with the updated tag
                    new_elem = ET.SubElement(backup_devices_element, new_tag)
                    copy_element(elem, new_elem)

        # Prettify the XML and write it to the output file
        # The old file is overwritten
        backup_pretty_xml = prettify_xml(backup_root)
        with open(BACKUP_FILE, "w", encoding="utf-8") as f:
            f.write(backup_pretty_xml)

        print(f"Exported XML saved to {BACKUP_FILE}")

    except Exception:
        exception_type, exception_object, exception_traceback = sys.exc_info()
        file = exception_traceback.tb_frame.f_code.co_filename
        line = exception_traceback.tb_lineno
        print("ERROR: " + f"{repr(exception_object)} of type {exception_type} in {file} line #{line}")
        exit(1)


def compare_and_update_elements(backup_elem, settings_elem, path):
    """
    Recursively compare the text and attributes of all elements and sub-elements
    between backup_elem and settings_elem.
    Path are all elements in the XML tree.
    Update settings_elem if differences are found.
    """
    # Compare text if not empty
    if backup_elem.text and backup_elem.text.strip() != (settings_elem.text or "").strip():
        print(f"|- Updating text of {path.rsplit("/", 1)[0]}/{settings_elem.tag} from '{settings_elem.text}' to '{backup_elem.text}'")
        write_to_dbus(path, backup_elem.text)
        # settings_elem.text = backup_elem.text

    # Compare attributes
    for attr, value in backup_elem.attrib.items():
        if settings_elem.attrib.get(attr) != value:
            print(f"|- Updating attribute '{attr}' of {settings_elem.tag} from '{settings_elem.attrib.get(attr)}' to '{value}'")
            # settings_elem.attrib[attr] = value

    # Recursively compare sub-elements
    backup_children = {child.tag: child for child in backup_elem}
    settings_children = {child.tag: child for child in settings_elem}

    for tag, backup_child in backup_children.items():
        if tag in settings_children:
            # Recursively compare matching sub-elements
            compare_and_update_elements(backup_child, settings_children[tag], f"{path}/{backup_child.tag}")
        else:
            # Add missing sub-elements from the backup to settings
            print(f"|- Adding missing sub-element {tag} to {settings_elem.tag}")
            # new_child = ET.SubElement(settings_elem, tag)
            # copy_element(backup_child, new_child)


def import_config():
    try:
        print(f"Restoring configuration from {BACKUP_FILE} to dbus")

        # Check if the backup file exists
        if not os.path.isfile(BACKUP_FILE):
            print(f"Backup file {BACKUP_FILE} does not exist. Aborting restore.")
            return

        # --- BACKUP FILE ---
        # Could also be a settings.xml copied from another device

        # Load the XML file
        backup_tree = ET.parse(BACKUP_FILE)

        # Get the root element of the XML tree
        backup_root = backup_tree.getroot()

        # Recursively sort the XML tree
        # This is needed to make sure, that devices with serial numbers are sorted
        sort_xml(backup_root)

        # Create empty list for devices
        backup_devices_list = []

        # Initialize serial number replacement logic
        backup_serial_number_map = {}
        next_serial_number = 1

        # Traverse the XML tree and extract matching subpaths
        for elem in backup_root.findall(".//Devices/*"):
            for pattern in PATTERNS:
                if elem.tag.startswith(pattern):
                    # print(f"Backup device {elem.tag} (matches {pattern})")

                    # Extract the serial number from the tag
                    match = SERIAL_REGEX.search(elem.tag)
                    if match:
                        original_serial = match.group(1)
                        if original_serial not in backup_serial_number_map:
                            # Generate a new serial number
                            new_serial = f"HQ{next_serial_number:09d}"
                            backup_serial_number_map[original_serial] = new_serial
                            next_serial_number += 1

                        # Replace the serial number in the tag
                        new_tag = elem.tag.replace(original_serial, backup_serial_number_map[original_serial])
                        # print(f"|- Updated tag: {elem.tag} -> {new_tag}")

                    # No serial number found, keep the tag as is
                    else:
                        new_tag = elem.tag

                    backup_devices_list.append(elem.tag)

        # --- SETTINGS FILE ---
        # Load the XML file
        settings_tree = ET.parse(SETTINGS_FILE)

        # Get the root element of the XML tree
        settings_root = settings_tree.getroot()

        # Recursively sort the XML tree
        # This is needed to make sure, that devices with serial numbers are sorted
        sort_xml(settings_root)

        # Create empty list for devices
        settings_devices_list = []

        # Initialize serial number replacement logic
        settings_serial_number_map = {}
        next_serial_number = 1

        # Traverse the XML tree and extract matching subpaths
        for elem in settings_root.findall(".//Devices/*"):
            for pattern in PATTERNS:
                if elem.tag.startswith(pattern):
                    # print(f"Settings device {elem.tag} (matches {pattern})")

                    # Extract the serial number from the tag
                    match = SERIAL_REGEX.search(elem.tag)
                    if match:
                        original_serial = match.group(1)
                        if original_serial not in settings_serial_number_map:
                            # Generate a new serial number
                            new_serial = f"HQ{next_serial_number:09d}"
                            settings_serial_number_map[original_serial] = new_serial
                            next_serial_number += 1

                        # Replace the serial number in the tag
                        new_tag = elem.tag.replace(original_serial, settings_serial_number_map[original_serial])
                        # print(f"|- Updated tag: {elem.tag} -> {new_tag}")

                    # No serial number found, keep the tag as is
                    else:
                        new_tag = elem.tag

                    settings_devices_list.append(new_tag)

        print()
        # Print the list as an ordered list
        print("# Backup devices:")
        for index, item in enumerate(backup_devices_list, start=1):
            print(f"{index}. {item}")
        print()
        # Print the list as an ordered list
        print("# Settings devices:")
        for index, item in enumerate(backup_devices_list, start=1):
            print(f"{index}. {item}")
        print()
        # Print the settings serial number map as key-value pairs
        print("# Settings serial number map:")
        for index, (key, value) in enumerate(settings_serial_number_map.items(), start=1):
            print(f"{index}. {key} - {value}")
        print()
        print()

        # Check if the backup devices are the same as the settings devices
        if set(backup_devices_list) != set(settings_devices_list):
            print("ERROR: Backup devices do not match settings devices. Aborting restore.")
            create_notification_gui(
                "Tank Backup Restore",
                "Backup devices do not match settings devices. Aborting restore.",
                1,
            )
            return

        # Compare the backup devices and settings devices
        for backup_elem in backup_root.findall(".//Devices/*"):
            for pattern in PATTERNS:
                if backup_elem.tag.startswith(pattern):
                    # print(f"Backup device {backup_elem.tag} (matches {pattern})")

                    # Extract the serial number from the tag
                    match = SERIAL_REGEX.search(backup_elem.tag)
                    if match:
                        backup_serial = match.group(1)

                        # Replace the serial number in the tag with the settings serial number
                        if backup_serial in settings_serial_number_map.values():
                            # Reverse lookup: find the key (settings serial) for the value (backup_serial)
                            settings_serial = [key for key, value in settings_serial_number_map.items() if value == backup_serial][0]
                            new_tag = backup_elem.tag.replace(backup_serial, settings_serial)
                            # print(f"|- Updated tag: {backup_elem.tag} -> {new_tag}")
                            settings_elem = settings_root.find(".//Devices/" + new_tag)
                        else:
                            new_tag = backup_elem.tag
                    else:
                        new_tag = backup_elem.tag

                    # Get the settings device
                    settings_elem = settings_root.find(".//Devices/" + new_tag)

                    # Compare recursively the text, if not empty, from all xml elements and its subelements
                    if settings_elem is not None:
                        print(f"Comparing device {settings_elem.tag} with backup device {new_tag}")
                        compare_and_update_elements(backup_elem, settings_elem, "/Settings/Devices/" + new_tag)
                    else:
                        print(f"Adding missing device {new_tag} to settings.xml")
                        new_device = ET.SubElement(settings_root.find(".//Devices"), new_tag)
                        copy_element(backup_elem, new_device)

    except Exception:
        exception_type, exception_object, exception_traceback = sys.exc_info()
        file = exception_traceback.tb_frame.f_code.co_filename
        line = exception_traceback.tb_lineno
        print("ERROR: " + f"{repr(exception_object)} of type {exception_type} in {file} line #{line}")
        exit(1)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--export", help="Export all tank setups to USB drive", default=False, action="store_true")
    parser.add_argument("--import", help="Import all tank setups from USB drive", default=False, action="store_true")
    parser.add_argument("--version", help="Print the version to stdout", default=False, action="store_true")
    args = parser.parse_args()

    if getattr(args, "version"):
        print("{} v{}".format(os.path.basename(sys.argv[0]), VERSION))
        return

    if getattr(args, "export"):
        export_setups()

    elif getattr(args, "import"):
        import_config()
    else:
        print("No action specified. Use --export or --import.")
        print("Use --help for more information.")


if __name__ == "__main__":
    main()
