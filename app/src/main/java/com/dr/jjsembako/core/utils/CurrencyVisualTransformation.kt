package com.dr.jjsembako.core.utils

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.core.text.isDigitsOnly
import java.text.NumberFormat
import java.util.Currency

/**
 * Visual filter for currency values. Formats values without fractions
 * adding currency symbol
 * based on the provided currency code and default Locale.
 * @param currencyCode the ISO 4217 code of the currency
 */
private class CurrencyVisualTransformation(
    currencyCode: String
) : VisualTransformation {
    /**
     * Currency formatter. Uses default Locale but there is an option to set
     * any Locale we want e.g. NumberFormat.getCurrencyInstance(Locale.ENGLISH)
     */
    private val numberFormatter = NumberFormat.getCurrencyInstance().apply {
        currency = Currency.getInstance(currencyCode)
        maximumFractionDigits = 0
    }

    override fun filter(text: AnnotatedString): TransformedText {
        /**
         * First we need to trim typed text in case there are any spaces.
         * What can by typed is also handled on TextField itself,
         * see SampleUse code.
         */
        val originalText = text.text.trim()
        if (originalText.isEmpty()) {
            /**
             * If user removed the values there is nothing to format.
             * Calling numberFormatter would cause exception.
             * So we can return text as is without any modification.
             * OffsetMapping.Identity tell system that the number
             * of characters did not change.
             */
            return TransformedText(text, OffsetMapping.Identity)
        }
        if (originalText.isDigitsOnly().not()) {
            /**
             * As mentioned before TextField should validate entered data
             * but here we also protect the app from crashing if it doesn't
             * and log warning.
             * Then return same TransformedText like above.
             */
            Log.w(
                "TAG",
                "Currency visual transformation require using digits only but found [$originalText]"
            )
            return TransformedText(text, OffsetMapping.Identity)
        }
        /**
         * Here is our TextField value transformation to formatted value.
         * EditText operates on String so we have to change it to Int.
         * It's safe at this point because we eliminated cases where
         * value is empty or contains non-digits characters.
         */
        val formattedText = numberFormatter.format(originalText.toInt())
        /**
         * CurrencyOffsetMapping is where the magic happens. See you there :)
         */
        return TransformedText(
            AnnotatedString(formattedText),
            CurrencyOffsetMapping(originalText, formattedText)
        )
    }
}

/**
 * CurrencyOffsetMapping is a class that maps offsets
 * between an original text and its formatted version.
 *
 * @param originalText The original unformatted text.
 * @param formattedText The formatted text, which has the same content
 *                      as originalText but with different
 *                      character positioning, due to added
 *                      or removed formatting characters.
 */
private class CurrencyOffsetMapping(originalText: String, formattedText: String) : OffsetMapping {
    private val originalLength: Int = originalText.length
    private val indexes = findDigitIndexes(originalText, formattedText)

    /**
     * Find the indexes of digits in the original text with respect
     * to the formatted text.
     *
     * @param firstString The original unformatted text.
     * @param secondString The formatted text.
     * @return A list of indexes indicating the position of digits
     *         in the secondString (formatted text).
     *         The order of indexes corresponds to the order of digits
     *         in the original text.
     *         If a digit is not found in the secondString,
     *         an empty list is returned.
     */
    private fun findDigitIndexes(firstString: String, secondString: String): List<Int> {
        val digitIndexes = mutableListOf<Int>()
        var currentIndex = 0
        for (digit in firstString) {
            // Find the index of the digit in the second string
            val index = secondString.indexOf(digit, currentIndex)
            if (index != -1) {
                digitIndexes.add(index)
                currentIndex = index + 1
            } else {
                // If the digit is not found, return an empty list
                return emptyList()
            }
        }
        return digitIndexes
    }

    /**
     * Maps an offset from the original text to its corresponding position
     * in the formatted text.
     *
     * @param offset The offset in the original text.
     * @return The offset in the formatted text corresponding to the input
     *         offset.
     *         If the input offset is beyond the length of the original text,
     *         the last position in the formatted text is returned adding 1
     *         to set the caret after last digit.
     */
    override fun originalToTransformed(offset: Int): Int {
        /**
         * Example:
         * original 123
         * formatted $123
         * indexes [1,2,3]
         * caret position/offset is 1 which is here 1|23 in the original
         * in formatted text it will be offset=2 since all digits move by 1
         * because of the $ symbol at start
         * if caret is at the end of 123 we do not have index for it in indexes
         * so we take last value from indexes and add 1
         */
        if (offset >= originalLength) {
            return indexes.last() + 1
        }
        return indexes[offset]
    }

    /**
     * Maps an offset from the formatted text to its corresponding position
     * in the original text.
     *
     * @param offset The offset in the formatted text.
     * @return The offset in the original text corresponding to the input
     *         offset.
     *         If the input offset is beyond the length of the formatted text,
     *         the length of the original text is returned.
     */
    override fun transformedToOriginal(offset: Int): Int {
        /**
         * Example 1:
         * original text 123
         * formatted text $123
         * indexes [1, 2, 3], index 0 is taken by $ symbol
         * if user tries to set caret before $ (offset = 0)
         * which is not allowed
         * we have to find the closest allowed caret position which in that
         * case will be 1
         *
         * Example 2:
         * original text 123
         * formatted text 123 USD
         * indexes [0,1,2] beyond that we have space and currency symbol
         * if user tries to set caret between U and S (offset=5)
         * which is not allowed
         * we have to find the closest allowed caret which we cannot in indexes.
         * Thus we take the length of original text to set caret after 123
         */
        return indexes.indexOfFirst { it >= offset }.takeIf { it != -1 } ?: originalLength
    }
}

/**
 * Helper function prevents creating CurrencyVisualTransformation
 * on every re-composition and use inspection mode
 * in case you don't want to use visual filter in Previews.
 * Currencies were displayed for me in Preview but I don't trust them
 * so that's how you could deal with it by returning VisualTransformation.None
 */
@Composable
fun rememberCurrencyVisualTransformation(currency: String): VisualTransformation {
    val inspectionMode = LocalInspectionMode.current
    return remember(currency) {
        if (inspectionMode) {
            VisualTransformation.None
        } else {
            CurrencyVisualTransformation(currency)
        }
    }
}