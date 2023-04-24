// 这里你可以定义任意多个列表。列表里的元素有两种，一种是域名，一种是 URL 模板。
//
// 其中域名是囊括子域名的，比如 "google.com" 就包含了 "www.google.com" 和 "docs.google.com"
// 等。域名用来匹配该域名及子域名下的所有地址。
//
// 域名中可以加星号来匹配任意内容，比如 cdn*.domain.com 包含了 cdn1.domain.com 和 cdn2.domain.com 等等。
//
// URL 模板中可以用通配符 * 来表示任意字符。URL 模板用来匹配更加精确的地址。


//////////////////////////////////////////////////////////////

var configuration = {
    proxies: "#PROXIES#",
    patternLists: "#PATTERN_LISTS#"
};

//////////////////////////////////////////////////////////////

// 浏览器将会调用的方法
// noinspection JSUnusedGlobalSymbols, JSUnusedLocalSymbols
function FindProxyForURL(url, host) {

    var proxies = configuration.proxies;
    var patternLists = configuration.patternLists;

    // 对 localhost 或其他不带 '.' 的域名（通常是内网机器名字）直接访问
    if (dnsDomainLevels(host) === 0) {
        // noinspection JSConstructorReturnsPrimitive
        return "DIRECT";
    }

    try {
        for (var i = 0; i < patternLists.length; i++) {
            var patternList = patternLists[i];
            var matchResult = doMatch(url, host, patternList.patterns);

            if (matchResult) {
                alert("[" + patternList.name + "] " + url);
            }

            if (matchResult) {
                var proxyName = patternList.proxyName;
                for (var j = 0; j < proxies.length; j++) {
                    if (proxies[j].name === proxyName) {
                        return parseProxy(proxies[j]);
                    }
                }
            }
        }
    } catch (e) {
        alert(e);
    }

    // noinspection JSConstructorReturnsPrimitive
    return "DIRECT";
}

function parseProxy(proxy) {
    if (proxy.type === 'DIRECT') {
        return "DIRECT";
    }

    return proxy.type + " " + proxy.host + ":" + proxy.port;
}

//////////////////////////////////////////////////////////////

// 检查 url 是否匹配域名模板
function matchDomains(host, pattern) {
    // .abc.com -> abc.com
    // abc -> abc.*

    if (pattern.charAt(0) === '.') {
        pattern = pattern.substring(1);
    } else if (pattern.indexOf(".") === -1) {
        pattern = pattern + ".*";
    }

    return shExpMatch(host, pattern) || shExpMatch(host, "*." + pattern);
}

// 判断模板是否是一个完整的 URL
function isURL(pattern) {
    return pattern.substring(0, 7) === 'http://' ||
        pattern.substring(0, 8) === 'https://' ||
        pattern.substring(0, 2) === '//';
}

// 检查 url 是否匹配 URL 模板
function matchUrls(url, pattern) {

    // 去掉开头
    if (pattern.substring(0, 7) === 'http://') {
        pattern = pattern.substring(7);
    } else if (pattern.substring(0, 8) === 'https://') {
        pattern = pattern.substring(8);
    } else if (pattern.substring(0, 2) === '//') {
        pattern = pattern.substring(2);
    }

    return shExpMatch(url, '*://' + pattern);
}

// 判断模板是否是一个 IP 地址
function isIP(pattern) {
    return pattern.match(/^\d+\.\d+\.\d+\.\d+$/);
}

function matchIP(url, ip) {
    if (!ip) {
        return false;
    }

    var startIndex = url.substring(0, 7) === "http://" ? 7 : (url.substring(0, 8) === "https://" ? 8 : -1);
    if (startIndex < 0) {
        return false;
    }

    url = url.substring(startIndex);
    return url.substring(0, ip.length) === ip;
}

// 检查指定的 url 是否与列表中的任何一项匹配
function doMatch(url, host, patternList) {
    try {
        for (var i = 0; i < patternList.length; i++) {
            var pattern = patternList[i];

            if (pattern === '') {
                continue;
            }

            if (isIP(pattern)) {
                if (matchIP(url, pattern)) {
                    return true;
                }
            } else if (isURL(pattern)) {
                if (matchUrls(url, pattern)) {
                    return true;
                }
            } else {
                if (matchDomains(host, pattern)) {
                    return true;
                }
            }
        }

        return false;
    } catch (e) {
        alert(e);
    }
}
