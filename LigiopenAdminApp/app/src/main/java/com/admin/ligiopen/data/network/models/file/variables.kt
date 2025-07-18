package com.admin.ligiopen.data.network.models.file

val fileData = FileData(
    fileId = 1,
    link = "https://ligiopen.s3.amazonaws.com/1738910578134_kasarani-stadium.jpg"
)

val emptyFileData = FileData(
    fileId = 0,
    link = ""
)

val fileDts = List(4) {
    FileData(
        fileId = 1,
        link = "https://ligiopen.s3.amazonaws.com/1738910578134_kasarani-stadium.jpg"
    )
}

val emptyFileDts = List(4) {
    FileData(
        fileId = 1,
        link = ""
    )
}