# Make nodejs use the shared version of openssl instead of building an internal
# one, to fix the execvp: printf: Argument list too long error, which happens
# (only) on machines where OE is in a long directory path. See also 
# https://github.com/imyller/meta-nodejs/issues/9

PACKAGECONFIG_append = " openssl"