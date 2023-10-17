#!/bin/sh

basedir=$(dirname $0)

notify () {
  if [ -d /service/dbus-characterdisplay ]; then
      cd /service && svc -d dbus-characterdisplay
  fi

  if [ -f ${basedir}/notify.py ]; then
    NOTIFY="${basedir}/notify.py"
  else
    NOTIFY="echo notify "
  fi

  ${NOTIFY} --message "${1}" $2

  if [ -d /service/dbus-characterdisplay ]; then
      cd /service && svc -u dbus-characterdisplay
  fi
}

