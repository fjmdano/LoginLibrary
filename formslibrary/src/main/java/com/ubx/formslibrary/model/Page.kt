package com.ubx.formslibrary.model

data class Page (var pageTitle: String,
                 var leftContent: Any?,
                 var rightContent: Any?,
                 var rows: MutableList<PageRow>)