# NOTE: the recipe comes form meta-homeassistant, but they only support OE master.
# https://github.com/meta-homeassistant/meta-homeassistant/commit/1914fd1e5ce9933a05350b0bccceb33038eb5fce

SUMMARY = "Fast, correct Python JSON library supporting dataclasses, datetimes, and numpy"
HOMEPAGE = "https://github.com/ijl/orjson"
LICENSE = "Apache-2.0 | MIT"
LIC_FILES_CHKSUM = " \
    file://LICENSE-APACHE;md5=1836efb2eb779966696f473ee8540542 \
    file://LICENSE-MIT;md5=b377b220f43d747efdec40d69fcaa69d \
"

SRC_URI += "file://fix-compilation-error-for-orjson.patch"
SRC_URI[sha256sum] = "d2aaa5c495e11d17b9b93205f5fa196737ee3202f000aaebf028dc9a73750f10"
require ${BPN}-crates.inc
S = "${WORKDIR}/git"

PYPI_PACKAGE = "orjson"

inherit cargo-update-recipe-crates pypi python_maturin

RDEPENDS:${PN} = "python3-zoneinfo"
