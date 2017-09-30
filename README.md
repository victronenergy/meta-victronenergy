meta-victronenergy
==================
This repository now contains several meta's, we tried to seperate the recipes
we provide and the changes we make to other layers. It turns out though that
for some packages this is not a trivial split, so this is a new attempt to
to keep some structure in the recipes. This retires [1].

NOTE: this is a flat repro, it should be possible to build for danny..yethro,
and Debian jessie / wheezy with the help of [2]. Only danny is actually tested /
officially released at the moment!

meta-alternatives
-----------------
Contains recipes of which we want a newer / different version (well at the time
of writing the recipe at least). e.g libevent, conmann and wpa-supplicant live
here, since this exact version is preferred; other versions are known to cause
problems. The priority of this layer should be high and PREFERRED_VERSION should
likely be provided. Over time alternative can become backports / are no longer
needed.

meta-backports
--------------
Contains recipes which are in upstream master, but upstream did not backport
it to older / no longer maintained versions. Obviously this should be kept as
small as possible or it will become a distro inside a distro. The file
meta-backports/backports.txt should mention the need to backport recipes, so
they can also be removed once no longer needed. The priority of this meta should
be low, so the normal recipe from openembedded-core / meta-openembedded is used
if the same version is available.

meta-bsp
--------
Provides the recipes to boot machines manufactured / shipped by victronenergy.
Typically bootloader + kernel. It shouldn't contain any "configuration", iow
another distro should be able to reuse it.

meta-third-party
----------------
Contains recipes for software not written by us, but also not in openembedded.
This can also overlaps in time. For example daemontools was not available in
danny, but is added in fido. So that actually makes it a backport now for danny
from fido.

meta-venus
----------
Contains our distro specific tweaks for openembedded. Typically bbappends go
here to make configuration choices and the like.

meta-ve-software
--------------------
Recipes for software provided by Victron Energy. It should not contain bbappends.
The intention is that this layer can be added to an existing oe setup.

NOTE: don't put recipes in this meta for projects which source code is not
(fully) publicly available, since it will cause build failures for people not
having access to them!

[1] https://github.com/victronenergy/meta-victronenergy-overlay
[2] https://github.com/jhofstee/meta-bin-deb
