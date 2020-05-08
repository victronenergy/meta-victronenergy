const bcrypt = require('bcryptjs')
const fs = require('fs')
const debug = require('debug')('user-authentication')

const adminUserName = 'admin'

module.exports = {
  type: "credentials",
  users: function(username) {
    return new Promise(function(resolve) {
      debug(`users: ${username}`)
      if ( username === adminUserName ) {
        resolve({ username: username, permissions: "*" })
      } else {
        resolve(null)
      }
    })
  },
  authenticate: function(username,password) {
    return new Promise(function(resolve) {
      debug(`authenticate: ${username}`)
      if ( username === adminUserName ) {
        const pw = getPassword()
        bcrypt.compare(password, pw, (err, matches) => {
          if (err) {
            console.error(err)
          resolve(null)
          } else if ( matches === true ) {
            debug('success')
          resolve({ username: username, permissions: "*" })
          } else {
            debug('no match')
            resolve(null)
          }
        })
      } else {
        debug('bad usernamw')
        resolve(null)
      }
    })
  },
  default: function() {
    return new Promise(function(resolve) {
      if ( getPassword() ) {
        debug('default: has password')
        resolve(null)
      } else {
        debug('default: no password')
        resolve({anonymous: true, permissions:"*"})
      }
    })
  }
}

function getPassword() {
  try {
    const data = String(fs.readFileSync('/data/conf/vncpassword.txt')).trim()
    return data.length > 0 ? data : null
  } catch (err) {
    console.err(err)
    return null
  }
}
