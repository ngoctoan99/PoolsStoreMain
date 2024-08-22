package com.pools.store.utils

object Constant {
    val BASE_URL_TRANSLATE = "https://ai-api.playgroundx.site/"
    const val DASHBOARD_PACKAGENAME = "com.pools.dashboard"
//    val BASE_URL = "https://airo-api-dev.aihomes.io/api/"
    val BASE_URL = "https://airo-api-stg.aihomes.io/api/"
    // tag include  : FOR_YOU, TRENDING, NEW_GAMES, PLAY_TO_EARN, LEADER_BOARD, OFFER, HOT_DISCOUNT, RECOMMEND, NO_INTERNET, POOLS_COLLECTION, ADS, HIGHT_LIGHTED
    val FOR_YOU = "FOR_YOU"
    val TRENDING = "TRENDING"
    val TOTAL_SUPPORT = "TOTAL_SUPPORT"
    val NEW_GAMES = "NEW_GAMES"
    val PLAY_TO_EARN = "PLAY_TO_EARN"
    val LEADER_BOARD = "LEADER_BOARD"
    val OFFER = "OFFER"
    val HOT_DISCOUNT = "HOT_DISCOUNT"
    val RECOMMEND = "RECOMMEND"
    val NO_INTERNET = "NO_INTERNET"
    val POOLS_COLLECTION = "POOLS_COLLECTION"
    val ADS = "ADS"
    val HIGHT_LIGHTED = "HIGHT_LIGHTED"
    val APPLICATION = "APPLICATION"
    val GAME = "GAME"

    /**
     * title see more
     * */
    val TITLE_FOR_YOU = "For You"
    val TITLE_TRENDING = "Trending"
    val TITLE_TOTAL_SUPPORT = "Total Support"
    val TITLE_NEW_GAMES = "New Games"
    val TITLE_PLAY_TO_EARN = "Play To Earn"
    val TITLE_LEADER_BOARD = "Leader Board"
    val TITLE_OFFER = "Offer"
    val TITLE_HOT_DISCOUNT = "Hot Discount"
    val TITLE_RECOMMEND = "Recommend"
    val TITLE_NO_INTERNET = "No Internet"
    val TITLE_POOLS_COLLECTION = "Pools Collections"
    val TITLE_ADS = "ADS"
    val TITLE_HEIGHT_LIGHTED = "Height lighted"

    val KEY_TYPE_FRAGMENT_GAME = "KEY_TYPE_FRAGMENT_GAME"
    val KEY_TYPE_FRAGMENT_APP = "KEY_TYPE_FRAGMENT_APP"



    val Open = "Open"
    val Update = "Update"
    val InstallPoint = "Install +18"
    val Install = "Install"
    val MINUS_POINT_WILL_SHOW_FEEDBACK = -5
    val LIMIT_IN_ONE_PAGE = 10
    val SPEECH_INPUT_COMPLETE_AFTER = 3_500

    val TIME_FOR_ANIMATION_IDLE = 30_000


    var isDisplayedPopupVersion=false   // hide popup update version , only show when it obligatory
    var isDisplayedPopupUrgent=false// hide popup urgent , only show when it obligatory

    var beforePointUserChange=-1
    var amountPointUserChange=-1
    var isDisplayedAnimationPoint=false// have show animation + - point



    const val DB_NAME = "airo.db"
    const val DB_VERSION = 3
    const val TABLE_POPUP_SCREEN_NAME = "popup_screen"

    const val TABLE_HISTORY_DIALOG_NAME = "history_dialog"

    const val CAMERA = "CAMERA"
    const val GALLERY = "GALLERY"

    const val Translation = "TRANSLATION"
    const val Overwrite  = "OVERWRITE"

    const val DEFAULT_VALUE_SPEECH_RATE =  1.0F
    const val DEFAULT_VALUE_SPEECH_PITCH =  1.0F



//     val allowedExtensions = listOf("pdf", "doc", "docx", "ppt", "pptx")  //extension for docs
val allowedExtensions = listOf("pdf", "docx", "pptx")  //extension for docs

}