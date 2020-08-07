import http from '@/util/http'

export function listTag() {
  return http({
    url: 'tags',
    method: 'get',
  });
}

export function addTag(tag) {
  return http({
    url: '/tags',
    method: 'post',
    data: tag
  });
}

export function updateTag(tag) {
  return http({
    url: '/tags',
    method: 'put',
    data: tag
  })
}

export function deleteTag(tagId) {
  return http({
    url: '/tags/'+tagId,
    method: 'delete'
  })
}