import http from '@/util/http'

export function listFriend() {
  return http({
    url: 'friends',
    method: 'get',
  });
}

export function addFriend(friend) {
  return http({
    url: 'friends',
    method: 'post',
    data: friend
  });
}

export function updateFriend(friend) {
  return http({
    url: 'friends',
    method: 'put',
    data: friend
  })
}

export function deleteFriend(friendId) {
  return http({
    url: 'friends/' + friendId,
    method: 'delete'
  })
}