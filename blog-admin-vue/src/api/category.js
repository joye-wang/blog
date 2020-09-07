import http from '@/util/http'

export function listCategory() {
  return http.get('categories');
}

export function addCategory(archive) {
  return http({
    url: '/categories',
    method: 'post',
    data: archive
  });
}

export function updateCategory(archive) {
  return http({
    url: '/categories',
    method: 'put',
    data: archive
  })
}

export function deleteCategory(archiveId) {
  return http({
    url: '/categories',
    method: 'delete',
    params: {
      archiveId
    }
  })
}