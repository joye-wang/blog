/**
 * @description 得到url中的参数
 * @param {String} name
 */
function getUrlParam(name) {
    var result = window.location.search.match(new RegExp("[\?\&]" + name + "=([^\&]+)", "i"));
    if (result == null || result.length < 1) {
        return "";
    }
    return result[1];
}
