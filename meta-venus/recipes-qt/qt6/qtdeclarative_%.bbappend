# Unless DEBUG_BUILD is enabled, pass -g1: debug info without local
# variables and types, which makes the build faster and use less memory.
DEBUG_FLAGS:append = "${@oe.utils.vartrue('DEBUG_BUILD', '', ' -g1', d)}"
