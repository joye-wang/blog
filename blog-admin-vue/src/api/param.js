import http from '@/util/http'

export function listParam() {
  return http.get('params');
}

export function updateParam(param) {
  return http({
    url: 'params',
    method: 'put',
    data: param
  });
}

export function createParam(param) {
  return http.post('params', param);
}

export function deleteParam(id) {
  return http.delete('params/' + id);
}

export function getValue(key) {
  return http.get('params/' + key);
}