package com.panasetskaia.storyarchitectlogline.data

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
        if (majorEvent!=null && majorEvent!=empty) {
            logline = "when ${prepareText(majorEvent)} "
        }
        if (worldText != null && worldText!=empty) {
            val newWorldText = worldText.replace("it is", "", ignoreCase = true)
            logline += "in ${prepareText(newWorldText)}, "
        } else if (majorEvent != null && majorEvent!= empty) {
            logline += ", "
        }
        logline += "${prepareText(characterInfo)} "
        logline += "must "
        if ((mprEvent == null || mprEvent== empty)&& (theme == null || theme== empty)) {
            logline += "${prepareText(storyGoal)} "
        } else if ((mprEvent == null || mprEvent== empty) && (theme != null && theme!= empty)) {
            logline += "in order to ${prepareText(theme)} "
            logline += "${prepareText(storyGoal)} "
        } else {
            logline += "${prepareText(storyGoal)}, "
            if (mprEvent != null && mprEvent!= empty) {
                logline += "but when ${prepareText(mprEvent)} $pronoun must "
            }
            if (theme != null && theme!= empty) {
                logline += "in order to ${prepareText(theme)} "
            }
            if (afterMprEvent != null && afterMprEvent!= empty) {
                logline += "${prepareText(afterMprEvent)} "
            }
        }
        if (stakes!=null && stakes!= empty) {
            logline += "before ${prepareText(stakes)}"
        }
        if (logline.last() != '.') {
            logline = logline.trim() + "."
        }
        logline = logline.capitalize()
        return logline
    }


    private fun prepareText(oldText: String): String {
        val trimmed = oldText.trim()
        var result = trimmed.replaceFirstChar {
            it.lowercase()
        }
        if (result.startsWith("to ")) {
            return result.substring(3)
        }
        return result
    }

    companion object {
        private const val empty = ""
    }

//    private fun buildLogline(): String {
//        val text = String({ textField ->
//            var text1: String = textField.getText().trim()
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