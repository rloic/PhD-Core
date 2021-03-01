package com.github.rloic.phd.core.files.csv

object CSV {

    fun formatLine(vararg values: Any?): CSVLine = CSVLine(buildString {
        for (value in values) {
            var strValue = value?.toString() ?: "null"

            if (strValue.contains('"')) {
                strValue = "\"" + strValue.replace("\"", "\"\"") + "\""
            }

            append(strValue)
            append(", ")
        }

        if (length > 2) {
            setLength(length - 2)
        }
    })

}