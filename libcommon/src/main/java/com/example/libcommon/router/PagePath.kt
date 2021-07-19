package com.example.libcommon.router

/**
 * @date: 2021-02-02 10:27 AM Tuesday
 */
object PagePath {
    const val ACTIVITY_PREFIX = "/activity_"
    const val FRAGMENT_PREFIX = "/fragment_"

    const val PARAM_SEPARATOR = "?"
    const val PARAM_CONNECTOR = "&"
    //页面是否需要用户登录标识
    const val LOGIN_KEY = "LOGIN_KEY"
    const val NEED_LOGIN = "1"

    const val PARAM_WEB_SHOW_TITLE = "showtitle=1"
    const val PARAM_WEB_IMMERSIVE = "isimmersive=1"

    /******************* group backdoor *******************/
    const val GROUP_BACKDOOR = "/backdoor"
    const val FRAGMENT_BACKDOOR = GROUP_BACKDOOR + FRAGMENT_PREFIX + "backdoor"
    const val FRAGMENT_BACKDOOR_GATE = GROUP_BACKDOOR + FRAGMENT_PREFIX + "backdoor_gate"
    const val FRAGMENT_DEVMODE_CUSTOM = GROUP_BACKDOOR + FRAGMENT_PREFIX + "devmode_custom"

    /******************* group nev_framework *******************/
    const val GROUP_NEVFRAMEWORK = "/nev_framework"
    const val WEB_FRAGMENT = GROUP_NEVFRAMEWORK + FRAGMENT_PREFIX + "web_fragment"
    const val NEVBASE_FRAGMENT_CONTAINER = GROUP_NEVFRAMEWORK + ACTIVITY_PREFIX + "fragment_container"
    const val NEVBASE_TRANS_FRAGMENT_CONTAINER = GROUP_NEVFRAMEWORK + ACTIVITY_PREFIX + "trans_fragment_container"

    /******************* group main *******************/
    const val GROUP_BOOT = "/boot"
    const val MAIN = GROUP_BOOT + ACTIVITY_PREFIX + "main"
    const val ACTIVITY_SPLASH = GROUP_BOOT + ACTIVITY_PREFIX + "splash"

    /******************* group main *******************/
    const val GROUP_MAIN = "/main"
    const val RECOMMEND = GROUP_MAIN + FRAGMENT_PREFIX + "recommend"

    /******************* group activities *******************/
    const val GROUP_ACTIVITY = "/activity"
    const val ACTIVITIES = GROUP_ACTIVITY + FRAGMENT_PREFIX + "activites"
    const val INSPIRATION = GROUP_ACTIVITY + FRAGMENT_PREFIX + "inspiration"

    /******************* group activities *******************/
    const val GROUP_POSTS = "/post"
    const val POSTS = GROUP_POSTS + FRAGMENT_PREFIX + "posts"

    /******************* group square *******************/
    const val GROUP_SQUARE = "/square"
    const val SQUARE = GROUP_SQUARE + FRAGMENT_PREFIX + "square"
    const val SQUARE_OLD = GROUP_SQUARE + FRAGMENT_PREFIX + "squareOld"

    /******************* group login *******************/
    const val GROUP_LOGIN = "/login"
    const val GROUP_REGISTER = "/register"
    const val REGISTER = GROUP_REGISTER + FRAGMENT_PREFIX + "registerFragment"
    const val LOGIN = GROUP_REGISTER + FRAGMENT_PREFIX + "loginFragment"
    const val AGREEMENT = GROUP_REGISTER + FRAGMENT_PREFIX + "agreementFragment"
    const val WXLOGIN = GROUP_LOGIN + ACTIVITY_PREFIX + "WXEntryActivity"

    /******************* group image *******************/
    const val GROUP_IMAGE= "/image"
    const val IMAGE_LIST = GROUP_IMAGE + FRAGMENT_PREFIX + "imageListFragment"

    /******************* group wechat *******************/
    const val GROUP_WECHAT= "/wechat"
    const val WE_CHAT = GROUP_WECHAT + FRAGMENT_PREFIX + "wechatFragment"

    /******************* ugc*******************/
    const val GROUP_UGC = "/ugc"
    const val FRAGMENT_PICTURE_PREVIEW = GROUP_UGC + FRAGMENT_PREFIX + "picture_preview"
    const val REPLY = GROUP_UGC + ACTIVITY_PREFIX + "reply"
    const val PUBLISH_MESSAGE = GROUP_UGC + FRAGMENT_PREFIX + "publish_message"
    const val EDIT_MESSAGE = GROUP_UGC + FRAGMENT_PREFIX + "publish_edit"
    const val MESSAGE_TAG = GROUP_UGC + FRAGMENT_PREFIX + "msg_tag"

    /******************* personal main *******************/
    private const val GROUP_PERSONAL = "/personal"
    const val PERSONAL_POINT = GROUP_PERSONAL + FRAGMENT_PREFIX + "point"
    const val PERSONAL_CENTER = GROUP_PERSONAL + FRAGMENT_PREFIX + "center" + PARAM_SEPARATOR + LOGIN_KEY + "=" + NEED_LOGIN
    const val FRAGMENT_OTHERS_ZONE = GROUP_PERSONAL + FRAGMENT_PREFIX + "others_zone"
    const val SETTING = GROUP_PERSONAL + FRAGMENT_PREFIX + "setting"
    const val ACCOUNT_MANAGER = GROUP_PERSONAL + FRAGMENT_PREFIX + "account_manager"
    const val MESSAGE_CENTER = GROUP_PERSONAL + FRAGMENT_PREFIX + "message_center"
    const val FEEDBACK = GROUP_PERSONAL + FRAGMENT_PREFIX + "feedback"

    /******************* group shortcuts *******************/
    private const val GROUP_SHORT_CUTS= "/shortcuts"
    const val SHORT_CUTS_ACTIVITY = GROUP_SHORT_CUTS + ACTIVITY_PREFIX + "short_cuts_activity"

    /******************* group CustomComponent *******************/
    private const val GROUP_CUSTOM_COMPONENT= "/CustomComponent"
    const val CUSTOM_COMPONENT_ACTIVITY = GROUP_CUSTOM_COMPONENT + ACTIVITY_PREFIX + "custom_component_activity"

}