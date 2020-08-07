import http from '@/util/http'

export function listCategory() {
  return http.get('categories');
}

export function addCategory(archive) {
  return http({
    url: '/categories/create',
    method: 'post',
    data: archive
  });
}

export function updateCategory(archive) {
  return http({
    url: '/categories/update',
    method: 'put',
    data: archive
  })
}

export function deleteCategory(archiveId) {
  return http({
    url: '/categories/delete',
    method: 'delete',
    params: {
      archiveId
    }
  })
}