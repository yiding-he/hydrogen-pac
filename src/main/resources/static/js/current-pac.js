// 这里你可以定义任意多个列表。列表里的元素有两种，一种是域名，一种是 URL 模板。
//
// 其中域名是囊括子域名的，比如 "google.com" 就包含了 "www.google.com" 和 "docs.google.com"
// 等。域名用来匹配该域名及子域名下的所有地址。
//
// 域名中可以加星号来匹配任意内容，比如 cdn*.domain.com 包含了 cdn1.domain.com 和 cdn2.domain.com 等等。
//
// URL 模板中可以用通配符 * 来表示任意字符。URL 模板用来匹配更加精确的地址。

// 全局白名单，直接访问
var whitelist = [
    "iuuq;00tubujd/dteo/ofu0qvcmjd0dpnnpo0uppmcbs0dtt0joefy/dtt",
    "iuuq;00tubujd/dteo/ofu0qvcmjd0dpnnpo0+", "bmjdeo",
    "471cvzjnh/dpn","mpdbmiptu","2:3/+","bmjqbz","21/21/+"];

// 黑名单中的白名单，翻墙访问
var whitelistgfw = [
    "iuuq;00be/uijtbw/dpn0qmbzfs0+","iuuq;00deo/mvtdjpvt/ofu0wjefpt0+",
    "iuuq;00+/mpohubjmwjefp/dpn0dspttepnbjo/ynm","iuuq;00tzoejdbujpo/fypdmjdl/dpn0qv/txg",
    "iuuq;00wt+/uijtbw/dpn0em0+","iuuqt;00xxx/htubujd/dpn0+","iuuqt;00xxx/hpphmf/dpn0+"];

// 黑名单
var blacklist = [
    "njyqbofm/dpn","kvjdzbet/dpn","dpoufoubcd/dpn","tozv/dpn","tffuijtjobdujpo/dpn",
    "beyqbotjpo/dpn","qpqbet/ofu","mpohubjmwjefp/dpn","ijtubut/dpn","qsxjehfut/dpn","csboesfbditzt/dpn",
    "jnmjwf/dpn","62/mb","dsbcebodf/dpn","bnvoh/vt","qvttzdbti/dpn","hmpcbmnbjmfs/dpn",
    "exusbdljoh/tep/dpn","hbnf/rjejbo/dpn","tusjq/ubpcbpdeo/dpn","qmvhsvti/dpn","b/bespmm/dpn",
    "ydz9/dpn","zy55:/dpn","dbdmjdl/tptp/dpn","211qfoh/dpn","be+/ofutifmufs/ofu","be{fsl/ofu",
    "+0bet0+","226/3:/89/29:","uboy/dpn","fypdmjdl/dpn","qufohjof/do","y/tptp/dpn","pvtot/ofu",
    "beojvn/dpn","53/73/77/24","kuyi/ofu","runpkp/dpn","be/dteo/ofu","runpkp/do","tubujd/dteo/ofu",
    "nfttbhf/dteo/ofu","00be/+/do0+","00bet/+/dpn0+","00be/+/dpn0+","00+/rjejbo/dpn0Qjduvsf0+/hjg",
    "00+/rjejbo/dpn0+/txg","00+/rjejbo/dpn0twobe0+/hjg","het897cpnzd/dpn","00in/cbjev/dpn0in/kt+"];

// 翻墙网址名单
var gfwlist = [
    "zpvuv/cf","cju/mz","u/dp","hpp/hm","yuvcf","75ujboxboh","xjljqfejb","xjljtpvsdf","xjljnfejb","xpseqsftt",
    "/xq/dpn","yjbpmbo/nf","ex/ef","/mjwf/dpn","hsfbugjsf","hsbwbubs","qpmznfs.qspkfdu","tibepxtpdlt","hpphmf",
    "cmphhfs","cmphtqpu","gudijoftf","mfutdpsq","cpuboxboh","bqqmfebjmz","gsffxfjcp","gbdfcppl","gcdeo","gcjnh",
    "uxjuufs","uxjnh","qvticvmmfu","hjuivc","hpphmfvtfsdpoufou","cpxfoqsftt","qmbzqdftps","gffecvsofs","hpphmfbqjt",
    "x4tdippmt","njofdsbgu","npohpec","nztrm","rvpsb","rvpsbdeo","jzpvqpsu","ccd","jopsfbefs","wpbdijoftf","bpmdeo",
    "wpbofxt","zpoibqofxt","sfvufst","sfvufstnfejb","zujnh","zpvuvcf","hpphmfwjefp","sgj/gs","bcpmvpxboh",
    "epxompbe/njdsptpgu/dpn","ibwf9/dpn","mzsjdtpvoeusbdl/dpn","jdpot9","mjwfdpejoh","sgb/psh","ofyunfejb","bcmxboh",
    "cboofecppl","jgu/uu","fdpopnjtu","tubujd.fdpopnjtu","dmpvegspou","nzwjtvbmjr","btbijdijoftf","ilfu",
    "ofxt/zbipp/dpn","cjudpjoubml","hbuf.qspkfdu","hsfbutfbdo","ttm.jnbhft.bnb{po","exofxt","brjdo","uijtbw",
    "mvtdjpvt","uvp9","mww3","xbdlz","ilhpmefo","mpdbmhvjeftdpoofdu","psbdmf","px/mz","ililil","fqpdiujnft","ozujnft",
    "ozu/dpn","cbfmevoh","ejtrvt","uifdpefxijtqfsfs","dispnftubuvt","dpefdpht","lffqbtt","sbqjenjofs","tusjqf",
    "hjuivcvtfsdpoufou","hjucpplt","evptivp/dpn","dijobejhjubmujnft","hfutzod","mjgfibdlfs","ujybuj","njdspczuft",
    "hfgpsdf","tfbhbuf","hhqiu","gteo/dpn","tpvsdfgpshf","xtk","ofucfbot","hju.tdn","uiftuboeofxt","tfdsfudijob",
    "hjuipxup","t.ntgu","qbp.qbp","cbdldijob","vojdpef.ubcmf","cboexbhpoiptu","nfejvn","gsftiufdiujqt","buhgx",
    "cju/ep","tnbsu3qbz","ivbhmbe","ofubmfsu","dijoftfqfo","wpod/nm","beeuijt","uifjojujvn","4euvcf","yyybojnfnpwjft",
    "bojnfuvcf","qmbzfsdeo","hfovjofuvcf","ywjefpt","ovwje","bggpsebcmfuvcf","qpsoivc","qiodeo","qpsoivc/qiodeo/dpn",
    "sfhjtufsuvcf","yibntufs","yideo","deo24","mfbqespje","boezspje","lfobj","boespje/dpn","ozu","ocdofxt","ocd",
    "cpohbdbnt",":cjt","muo","op/dpn","hfutibsfy","njo{iv{ipohhvp","nbeplp","h/dp","hfunem/jp","xjuihpphmf","cpyvo",
    "wjnfp","hjucppl","quu/dd","jnhvs","cpplt/dpn/ux","sfbenpp","tznmftt","hsbemf","gpscft","ubmlnfufdi","uvncms",
    "xjmmjbnmpoh","jowjtjpobqq","{fsbopf/dpn","njohqbp","bupn/jp","e{pof","sgj/nz","kbwb3t","u77z","tibsqebjmz",
    "jotubhsbn","gmbd.mpttmftt","hpphmfhspvqt","qjyofu","kfucsbjot","tupsn/nh","hbnfs/dpn/ux","cppnt","xtjnh","culv",
    "ex/dpn","tqsjoh/jp","gjtidpefmjc","xfoyvfdjuz","veo/dpn","tubsvnm","lydeo","xtkfnbjm","qpsubcmfbqqt","blbnbjie",
    "cppntt","{fspofu","jqgt/jp","mbvodiqbemjcsbsjbo/ofu","xfc/bsdijwf/psh","gsffofuqspkfdu","ibojnf/uw",
    "gffesfbefs/dpn","oueuw/dpn","hpujoefs/dpn","ifoubjgju","iuuqt;00pqfompbe/dp0tusfbn0+","gsffxfdibu","kbwnpwjf",
    "nzdijoftf/ofxt","qptujnh/psh","xtk/dpn","gffemz","gmjqcpbse","tubujdxpsme/ofu","ypomjof/uw","uvsumbqq/dpn",
    "kljtt/psh","judi/jp","gppcbs3111/psh","obccmf/dpn","ozutuzmf/dpn","ebubtuby","hvsv::","wjnfpdeo","smdeo",
    "qzuipo","sfejsfdufs/psh","sjpu","esjqfnbjm3","qspkfdumpncpl","vefnz","bnb{pobxt","pggjdf/ofu","cmppncfsh",
    "xegjmft","do/sgj/gs","diboofm9ofxt","tjodifx","bxttubujd","upqcftubmufsobujwft","sbqjehbups","xqwjtjpo",
    "nwosfqptjupsz","dpvdicbtf","hpw/ux","kbohp/dpn","sbejpbjsqmbz","tpvoedmpve","toedeo","rvboutfswf","uvncms/dpn",
    "nfodmvc","cmphcmph","evdlevdlhp","qbtufcjo","qjolcjlf","upssfouepxompbet","dpoufoubcd","hppesfbet","fopuft",
    "jogprtubujd","tujdlzqbttxpse","{bpcbp","nfejb.bnb{po","yuvcf/dpn","ipkfokfo","gmjdls","zbipp","dpejohipssps",
    "diptvo","yeb.efwfmpqfst","boespjedfousbm","sfbeuifepdt","pqfodw","nzdpoubdut.bqq","opsfejol","btqofudeo",
    "np{jmmb","qvsvsjo","uifofxtmfot","qpmznfsil","sfejt","tvqfstqffe","bmufsobujwfup","uflophbezfu","gjoeofse",
    "b{vsffehf","kbwbgyuvut","dijuhplt","cvccmfdmjqt","xpxhjsmt","vqmpbefe/ofu","tipqjgz","dmpvegmbsf","esjq/mb",
    "tdnq","wjdf","u/vncms/dpn","bllb/jp","uffoqpsohbmmfsz","ejbozjohyjo","gmpxifbufs","xjottiufsn","lffqwje",
    "il12","tpobuzqf","qspkfdu.tzoejdbuf","gmx","cpy","tubdlfydibohf","b{bejofuxpsl","ojmfkt","/dpn/ux","{eixfc",
    "t4/bnb{pobxt/dpn","tmjub{","djoubopuft","qmvncs","nzqpsubcmftpguxbsf","tufbnqpxfsfe","hgx/qsftt","w3sbz",
    "ejtq/dd","uxjcff/dpn","usvfnpwjf","tufbndpnnvojuz","il/po/dd","sfeeju","sfeejutubujd","sfeejunfejb",
    "mfbqnpujpo","tmjeftibsf","je/do/xtk/dpn","fdmjqtf/psh",":2qpso","hsbemf/psh","ejtdpvstf/psh","hjuivcbqq",
    "tg/ofu","qmbzebvoumftt","kbwb/ofu","csjbsqspkfdu","fggjddftt","effqmfbsojoh5k","vebdjuz","dpvstfsb",
    "doe/psh","iyxl","gmzdijob","fofxtusff","ubohiv{j/dmvc","tfuo","uzqflju","mjofbhfpt","mjofbhfptspn",
    "hxuqspkfdu","espqcpy","tbwf.wjefp","qjoufsftu","qjojnh","efwjboubsu","gyfyqfsjfodf","hvjhbsbhf","ufmfhsbn",
    "i3z/dp","ifrjohmjbo","cv{{psbohf","gmbujdpo","ufmfhsb","qpsousfy","tpvsdfgpshf/ofu","qfodjm/fwpmvt/wo",
    "pggjdfdeo/njdsptpgu/dpn","mjcsfpggjdf","iuuq;00+/nfejb/uvncms/dpn0+","dpouspmtgy","hjwfzpvtt","tdsffoqsfttp",
    "+;00+/sodeo4/dpn0wjefpt0+","gsbtfsdpbtudispojdmf","htubujd/dpn","epvc/jp","epdt/njdsptpgu/dpn","ifoubjtdbot",
    "nbusjy/psh","bumbttjbo","cjucvdlfu","tbgfofugpsvn","nbjetbgf","spcfsuttqbdfjoevtusjft","tqjefspbl",
    "dfsucpu/fgg/psh","deo/np{jmmb/ofu","dob/dpn/ux","jnqsphsbnnfs","u/nf","ojdp/pof","ojdp/dbgf","kkhjsmt",
    "4qspyz/sv","xjohbuf","deojotubhsbn","vsm/hpphmf/dpn","gsffjodijob/psh","qjyjw","ojdfnpf","ueftlupq","tjohmpwf",
    "cjousbz","wl/dpn","eotdszqu","y/dpnqboz","obozbohqptu","csfblhgx","nzdijobofxt","ohjoy/dpn","{foeftl","sbxhju",
    "tjohubp","ojllfj","ozudo/nf","gbdfcppl/dpn","i3ebubcbtf","uxsq","fmbtujd","opx/dpn","fyusfnfufdi","tfoewje",
    "bmmjufcpplt","bsdicpz","dijob6111","vcj/mj","bqbdif","mjwf","tlzqf","pqfokel","ux/ofxt/bqqmfebjmz/dpn",
    "bnqqspkfdu","nzcbujt","kjlfff","rjxfo/mv","sfedijobdo","doo/dpn","gsffejg/psh","qmvtnfttfohfs","dpowfsu3nq4",
    "upy/dibu","bqqmfebjmz/dpn","qjo.dpoh","d3/dpn","bsdijwf/gp","ojoufoep","tlzqf/dpn","mjwf/dpn","cpputusbqdeo",
    "blbnbj{fe","hgy/nt","poftupsf/nt","blb/nt","tipudvu/psh","npwbwj/dpn","wjefptpguefw/dpn","dmpve/hpphmf/dpn",
    "gmvuufs/jp","tzodboz/psh","psjfouec/dpn","xjoepxt/ofu","hfuitupsf/cmpc/dpsf/xjoepxt/ofu","tzod/dpn",
    "uifejwjtjpohbnf","ebufolpmmflujw/ef","nfhb/o{","spxbo/fev","zpvuvcf.opdppljf","ibqspyz","qbububq","kbwb78",
    "bpm/dpn","tvswfzhj{np","wjefpmbo","mjcef376","ywjefpt.deo","gjsfcbtfjp","dbmjcsf.fcppl/dpn","dpnnvojmjol",
    "zbn/dpn","tfdsfudijob/dpn","xxx/tfdsfudijob/dpn","ueftlupq/dpn","suil/il","bjswjtvbm/dpn","dpefmpbe/hjuivc/dpn",
    "ufmfhsb/qi","sftfbsdihbuf/ofu","shtubujd","ujnf/dpn","vnv/tf","xb/vt","dsfbefst/ofu","gjmf{jmmb.qspkfdu/psh",
    "ubjxboljtt","dpogjefoutztufnt","ibqqztlz","lbmj/psh","dut/dpn/ux","xxx/hpphmf/dpn","i639","ycppldo","7:tupsz",
    "gsff/gs","blbnbjie/ofu","{bpcbp/dpn/th","hdpsfmbct","ufmfhsbqi","ejtdpsebqq","ibdh","tnbsuipnfcfhjoofs","njucct",
    "ttm.jnbhft.bnb{po/dpn","tmby","bn841","ccd/dpn","ccdj/dp/vl","ozuj/nt","ijcfsobuf/psh","uifopvoqspkfdu",
    "gfbuifsjdpot","3neo","nbufsjbm/jp","hvp/nfejb","kvmjbmboh","psjhjo","xjoez","hjulsblfo","odid",
    "epdvnfougpvoebujpo","pt7","yvfivb","dbjmjboqsftt","hph","hsplpof{","hmjggz","jnfebp","rpt/di","qjdbdpnjd",
    "nfmupebz","upsupjtfhju","mjufqbqfs","tpguqfejb","kpeb","gpoubxftpnf","uifwfshf","xjsfe","gffetlz","jnnvtpvm",
    "ecfbwfs","sfee/ju","cxcy/jp","{ebttfut","hffuftu","epxkpoft","tpvoepgipqf","hbkjn","pqfokgy","jdpnppo",
    "jpojdpot","vtfjdpojd","txbhhfs","dzhxjo","suj/psh/ux","sfvufst/uw","wvfkt","npohpcpptufs","hpphmfubhnbobhfs",
    "em/tpvsdfgpshf/ofu","izqfsjd/dpn","sfdbqudib","hnbjm/dpn","dpeftubut","tubsuqbhf/dpn","bwh.fohjof/dpn",
    "dpowfstbujpot/jn","ybccfs","g.espje/psh","hfupvumjof","nppo4enbq","xfckbst","pggjdf476"];

///////////////////////////////////////////////

// 场景配置
// 这里定义代理选择的过滤规则，靠前的优先级最高。
var PROFILES = {
    'default': [
        {list: whitelist,    proxy: "DIRECT"},
        {list: whitelistgfw, proxy: "SOCKS5 127.0.0.1:7890"},
        {list: blacklist,    proxy: "PROXY 127.0.0.1:1"},
        {list: gfwlist,      proxy: "SOCKS5 127.0.0.1:7890"}
    ]
};


// *********************************************
var CURRENT_PROFILE = PROFILES['default'];
// *********************************************

// 全局缺省，当地址不匹配任何一个列表时，返回缺省值
var DEFAULT_PROXY_RESULT = "DIRECT";

///////////////////////////////////////////////

var convert = function (str, forward) {
    var result = "";
    for (var i = 0; i < str.length; i++) {
        var charcode = str.charCodeAt(i);
        result += String.fromCharCode(charcode + forward);
    }
    return result;
};

function getDomain(url) {

    if (url.substring(0, 8) == 'file:///') {
        return url;
    } else if (url.substring(0, 7) == 'http://') {
        url = url.substring(7);
    } else if (url.substring(0, 8) == 'https://') {
        url = url.substring(8);
    }

    var slashIndex = url.indexOf("/");
    if (slashIndex != -1) {
        url = url.substring(0, slashIndex);
    }
    return url;
}

// 检查 url 是否匹配域名模板
function matchDomains(url, pattern) {
    var domain = getDomain(url);

    // .abc.com -> abc.com
    // abc -> abc.*

    if (pattern.charAt(0) == '.') {
        pattern = pattern.substring(1);
    } else if (pattern.indexOf(".") == -1) {
        pattern = pattern + ".*";
    }

    return shExpMatch0(domain, pattern) || shExpMatch0(domain, "*." + pattern);
}

// 判断模板是否是一个完整的 URL
function isURL(pattern) {
    return pattern.substring(0, 7) == 'http://' ||
        pattern.substring(0, 8) == 'https://' ||
        pattern.substr(0, 2) == '//';
}

// 检查 url 是否匹配 URL 模板
function matchUrls(url, pattern) {

    // 去掉开头
    if (pattern.substring(0, 7) == 'http://') {
        pattern = pattern.substring(7);
    } else if (pattern.substring(0, 8) == 'https://') {
        pattern = pattern.substring(8);
    } else if (pattern.substring(0, 2) == '//') {
        pattern = pattern.substring(2);
    }

    return shExpMatch0(url, '*://' + pattern);
}

// 判断模板是否是一个 IP 地址
function isIP(pattern) {
    return pattern.match(/^\d+\.\d+\.\d+\.\d+$/);
}

function matchIP(url, ip) {
    if (!ip) {
        return false;
    }

    var startIndex = url.substring(0, 7) == "http://" ? 7 : (url.substring(0, 8) == "https://" ? 8 : -1);
    if (startIndex < 0) {
        return false;
    }

    url = url.substring(startIndex);
    return url.substring(0, ip.length) == ip;
}

// 检查指定的 url 是否与列表中的任何一项匹配
function doMatch(url, patternList) {
    try {
        for (var i = 0; i < patternList.length; i++) {
            var pattern = convert(patternList[i], -1);

            if (pattern == '') {
                continue;
            }

            if (isIP(pattern) && matchIP(url, pattern)) {
                return true;
            } else if (isURL(pattern) && matchUrls(url, pattern)) {
                return true;
            } else if (matchDomains(url, pattern)) {
                return true;
            }
        }
    } catch (e) {
        if (window.console) {
            window.console.log(e);
        }
    }
}

// 浏览器将会调用的方法
// noinspection JSUnusedGlobalSymbols, JSUnusedLocalSymbols
function FindProxyForURL(url, host) {

    try {
        for (var i = 0; i < CURRENT_PROFILE.length; i++) {
            var level = CURRENT_PROFILE[i];

            if (doMatch(url, level.list)) {
                return level.proxy;
            }
        }
    } catch (e) {
        alert(e);
    }

    // noinspection JSConstructorReturnsPrimitive
    return DEFAULT_PROXY_RESULT;
}

function startsWith(str, search) {
    return str && search && str.substring(0, search.length) == search;
}

// "Better implementation of shExpMatch"
// https://gist.github.com/ayanamist/2989518
function shExpMatch0(url, pattern) {
    var pCharCode;
    var isAggressive = false;
    var pIndex;
    var urlIndex = 0;
    var lastIndex;
    var patternLength = pattern.length;
    var urlLength = url.length;
    for (pIndex = 0; pIndex < patternLength; pIndex += 1) {
        pCharCode = pattern.charCodeAt(pIndex); // use charCodeAt for performance, see http://jsperf.com/charat-charcodeat-brackets
        if (pCharCode === 63) { // use if instead of switch for performance, see http://jsperf.com/switch-if
            if (isAggressive) {
                urlIndex += 1;
            }
            isAggressive = false;
            urlIndex += 1;
        }
        else if (pCharCode === 42) {
            if (pIndex === patternLength - 1) {
                return urlIndex <= urlLength;
            }
            else {
                isAggressive = true;
            }
        }
        else {
            if (isAggressive) {
                lastIndex = urlIndex;
                urlIndex = url.indexOf(String.fromCharCode(pCharCode), lastIndex + 1);
                if (urlIndex < 0) {
                    if (url.charCodeAt(lastIndex) !== pCharCode) {
                        return false;
                    }
                    urlIndex = lastIndex;
                }
                isAggressive = false;
            }
            else {
                if (urlIndex >= urlLength || url.charCodeAt(urlIndex) !== pCharCode) {
                    return false;
                }
            }
            urlIndex += 1;
        }
    }
    return urlIndex === urlLength;
}
