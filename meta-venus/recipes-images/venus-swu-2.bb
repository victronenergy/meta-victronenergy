require venus-swu.inc

SRC_URI:append:canvu500 += "\
    file://ubi-install.sh \
"

ROOT_FSTYPE:canvu500 = "tar.gz"
