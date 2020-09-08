import http from '@/util/http'

export function listPost(condition) {
  return http({
    url: 'posts',
    params: condition
  });
}

export function getPost(postId) {
  return http.get('posts/'+postId)
}

export function movePost(postId, newCategoryId) {
  return http({
    url: 'posts/category',
    method: 'put',
    params: {
      postId,
      newCategoryId
    },
    type: 'form'
  });
}

export function toggleStatus(postId,status) {
  return http({
    url: 'posts/status',
    method: 'put',
    data: {
      postId,
      status
    },
    type: 'form'
  });
}

export function deletePost(postId) {
  return http({
    url: 'posts/'+postId,
    method: 'delete',
  });
}

export function publishPost(post) {
  return http({
    url: 'posts',
    method: 'post',
    data: post
  });
}

export function editPost(post) {
  return http({
    url: 'posts',
    method: 'put',
    data: post
  })
}