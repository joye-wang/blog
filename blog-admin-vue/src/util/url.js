export function getUrlParam(name) {
  let address = window.location.href;
  address = address.substring(address.indexOf('?') + 1);
  var start = address.indexOf(name);
  if (start < 0) return "";
  // 未找到
  if (address.charAt(start + name.length) !== '=')
    return "";
  var next = address.substring(start).indexOf("&");
  if (next < 0) {
    return address.substring(start + name.length + 1);
  } else {
    return address.substring(start + name.length + 1, next);
  }
}

export function openUrl(url) {
  window.open(url);
}