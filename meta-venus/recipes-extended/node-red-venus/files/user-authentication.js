const bcrypt = require('bcryptjs')
const fs = require('fs')
const debug = require('debug')('user-authentication')

const adminUserName = 'admin'
const passwordFileName = '/data/conf/vncpassword.txt'

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
        if (pw === '') {
          // If password file exists but is empty, authentication passes
          debug('empty password file, authentication passes')
          resolve({ username: username, permissions: "*" })
        } else if (pw === null) {
          // If password file doesn't exist, follow existing logic
          debug('password file does not exist')
          resolve(null)
        } else {
          // Normal password check with bcrypt
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
        }
      } else {
        debug('bad username')
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
    const data = String(fs.readFileSync(passwordFileName)).trim()
    // Return the actual data, even if it's an empty string
    return data
  } catch (err) {
    if ( err.errno === -2 ) {
      debug(`${passwordFileName} does not exist (yet)`);
      return null
    }
    console.error(err)
    return null
  }
}
