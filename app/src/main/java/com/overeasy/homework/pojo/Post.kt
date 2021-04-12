package com.overeasy.homework.pojo

data class Post(
    val id: String,
    var title: String,
    var body: String
    // title과 body는 update할 때 Dialog에서 수정되어야 하므로 var로 선언.
)