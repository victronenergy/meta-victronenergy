meta-victronenergy
==================
This repository contains several meta's.

meta-alternatives
-----------------
Contains recipes of which we want a newer / different version (well at the time
of writing the recipe at least). e.g libevent, conmann and wpa-supplicant live
here, since this exact version is preferred; other versions are known to cause
problems. The priority of this layer is high.

meta-bsp
--------
Provides the recipes to boot machines manufactured / shipped by victronenergy.
Typically bootloader + kernel. It shouldn't contain any "configuration", iow
another distro should be able to reuse it.

meta-third-party
----------------
Contains recipes for software not written by us, but also not in openembedded.
This can also overlaps in time. For example daemontools was not available in
danny, but is added in fido. Recipes from bsp layers which do not want to
include completely should also end up here.

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
