#!/bin/bash

set -e
set -o pipefail

hash="$(gunzip -c /var/www/venus/gui-beta/venus-gui-v2.wasm.gz | sha256sum | awk '{ print $1; }')"
echo "$hash"
