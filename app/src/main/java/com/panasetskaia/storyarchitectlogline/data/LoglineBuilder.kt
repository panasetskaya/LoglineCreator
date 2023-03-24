package com.panasetskaia.storyarchitectlogline.data

import android.util.Log

class LoglineBuilder(
    private val pronoun: String,
    private val majorEvent: String?,
    private val storyGoal: String,
    private val majorEventIncludesMainCharacter: Boolean,
    private val characterInfo: String,
    private val theme: String?,
    private val mprEvent: String?,
    private val afterMprEvent: String?,
    private val stakes: String?,
    private val worldText: String?,
) {

    fun buildLogline(): String {
        var logline = ""
        majorEvent?.let {
            logline = "when ${prepareText(it)}, "
            Log.e("my_tag", "when added")
        }
        worldText?.let {
            val newWorldText = if (!it.contains(" world where")) {
                it.replace("it is", "")
            } else it
            logline += "in ${prepareText(newWorldText)} "
        }
        logline += "${prepareText(characterInfo)} "
        logline += "must "
        if (mprEvent==null && theme==null) {
            logline += "${prepareText(storyGoal)} "
        } else {
            if (mprEvent==null) {
                logline += " in order to ${theme?.let { prepareText(it) }} "
                logline += "${prepareText(storyGoal)} "
            } else {
                logline += "${prepareText(storyGoal)}, "
                logline += "but when ${prepareText(mprEvent)} $pronoun must "
                logline += "in order to ${theme?.let { prepareText(it) }} "
                logline += "${afterMprEvent?.let { prepareText(it) }} "
            }
        }
        stakes?.let {
            logline += "before ${prepareText(it)}"
        }
        if (logline.last()!='.') {
            logline = logline.trim() + "."
        }
        logline = logline.capitalize()
        return logline
    }


    private fun prepareText(oldText: String): String {
        var result = oldText.trim().lowercase()
        if (result.startsWith("to ")) {
            return result.substring(3)
        }
        return result
    }

//    private fun buildLogline(): String {
//        val text = String({ textField ->
//            var text1: String = textField.getText().trim() //todo: вот это какой textField?
//            if (text1.startsWith("to ", Qt.CaseInsensitive)) {
//                text1 = text1.mid(3)
//            }
//            if (text1.length > 0) {
//                text1[0] = text1[0].toLower()
//            }
//            text1
//        })
//        var logline: String
//        val characterPronoun: String = if (characterGender.currentIndex()
//                .row() === 0
//        ) tr("he") else if (characterGender.currentIndex().row() === 1) tr("she") else tr("it")
//
//        // Q1
//        if (!majorEvent.text().isEmpty()) {
//            logline += QString("%1 %2, ").arg(tr("When"), text(majorEvent))
//        }
//        // Q8
//        if (worldHasSpecialRules.isChecked()) {
//            val worldText: String = text(worldSpecialRules)
//            if (!worldText.contains(" world where", Qt.CaseInsensitive)) {
//                logline = logline.remove("it is", Qt.CaseInsensitive)
//            }
//            logline += QString("%1 %2, ").arg(tr("in"), worldText)
//        }
//        // Q1 && Q8
//        if (!majorEvent.text().isEmpty() && majorEventIncludesMainChanager.isChecked()) {
//            logline += characterPronoun
//        } else {
//            logline += text(characterInfo)
//        }
//        //
//        logline += QString(" %1 ").arg(tr("must"))
//        // !Q7 && !Q5
//        if (!includeMpr.isChecked() && !includeTheme.isChecked()) {
//
//            // Q5
//            if (includeTheme.isChecked()) {
//                logline += QString("%1 %2 ").arg(text(theme), tr("in order to"))
//            }
//
//            logline += QString("%1 ").arg(text(storyGoal))
//        } else {
//            // !Q7 && Q5
//            if (!includeMpr.isChecked() && includeTheme.isChecked()) {
//                logline += QString("%1 %2 ").arg(text(theme), tr("in order to"))
//            }
//            //
//            logline += QString("%1").arg(text(storyGoal))
//            // Q7
//            if (includeMpr.isChecked()) {
//                logline = logline.trimmed() // ??????? ?????? ? ?????
//                logline += QString("; %1 %2 %3, %4 %5 ")
//                    .arg(tr("but"), tr("when"), text(mprEvent), characterPronoun, tr("must"))
//                // Q5
//                if (includeTheme.isChecked()) {
//                    logline += QString("%1 %2 ").arg(text(theme), tr("in order to"))
//                }
//                logline += QString("%1 ").arg(text(afterMprEvent))
//            } else {
//                logline += " "
//            }
//        }
//        // Q9
//        if (includeStakes.isChecked()) {
//            logline += QString("%1 %2 ").arg(tr("before"), text(stakes))
//        }
//
//        logline = logline.remove(".").simplified().trimmed() + "."
//        if (logline.length > 0 && logline.at(0).toUpper() !== logline.at(0)) {
//            logline[0] = TextHelper.smartToUpper(logline.at(0))
//        }
//        return logline
//    }
}