import http from '@/util/http'

export function resetPwd(oldPwd, newPwd) {
  return http({
    url: '/admins/resetPwd',
    method: 'post',
    data: {
      oldPwd,
      newPwd
    }
  });
}

export function login(username, password) {
  return http({
    url: '/admins/login',
    method: 'post',
    data: {
      username: username,
      password: password
    }
  });
}

export function logout() {
  return http({
    url: '/admins/logout',
    method: 'post'
  });
}