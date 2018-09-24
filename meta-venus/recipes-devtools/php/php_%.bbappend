FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

PACKAGECONFIG="sqlite3"

# remove these, when mysql is not enabled building 7.2.9 fails with
# undefined reference to symbol 'dlopen@@GLIBC_2.4' otherwise.
CACHED_CONFIGUREVARS_remove = "ac_cv_func_dlopen=yes"
CACHED_CONFIGUREVARS_remove = "ac_cv_lib_dl_dlopen=yes"
