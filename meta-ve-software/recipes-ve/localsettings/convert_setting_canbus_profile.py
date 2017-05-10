#!/usr/bin/python

from lxml import etree
import os.path

def delete(tree, path):
	obj = tree.xpath(path)
	if not obj:
		return
	obj[0].getparent().remove(obj[0])

def can_profile():
	if not os.path.isfile("/etc/venus/canbus_ports"):
		return

	if not os.path.isfile("/data/conf/settings.xml"):
		return

	with open('/etc/venus/canbus_ports', 'r') as f:
    		interface = f.readline().split(None, 1)[0]
	print("checking profile for " + interface)
	path = "/Settings/Canbus/" + interface + "/Profile"

	parser = etree.XMLParser(remove_blank_text=True)
	root = etree.parse("/data/conf/settings.xml", parser)

	if root.xpath(path):
		print(path + " already exists!")
		return

	# default to Ve.Can
	profile = 1

	if root.xpath("/Settings/Services/LgResu/text()") == ["1"]:
		profile = 3
	elif root.xpath("/Settings/Services/OceanvoltMotorDrive/text()") == ["1"] or \
		root.xpath("/Settings/Services/OceanvoltValence/text()") == ["1"]:
		profile = 4
	elif root.xpath("/Settings/Services/VeCan/text()") == ["0"]:
		profile = 0

	print("Setting " + path + " to " + str(profile))
	settings = root.getroot()
	canbus = settings.find("Canbus")
	if canbus == None:
 		canbus = etree.SubElement(settings, "Canbus")

	inter = canbus.find(interface)
	if inter == None:
		inter = etree.SubElement(canbus, interface)

	prof = etree.SubElement(inter, "Profile")
	prof.text = str(profile)
	prof.set('type', 'i')

	delete(root, "/Settings/Services/LgResu")
	delete(root, "/Settings/Services/OceanvoltMotorDrive")
	delete(root, "/Settings/Services/OceanvoltValence")
	delete(root, "/Settings/Services/VeCan")

	root.write('/data/conf/settings.xml.tmp', pretty_print=True, xml_declaration=True, encoding="UTF-8")
	os.rename("/data/conf/settings.xml.tmp", "/data/conf/settings.xml")

can_profile()
