package com.dr.jjsembako.core.utils.chart

import android.graphics.RectF
import com.dr.jjsembako.core.utils.formatRupiah
import com.patrykandpatrick.vico.core.chart.dimensions.HorizontalDimensions
import com.patrykandpatrick.vico.core.chart.insets.Insets
import com.patrykandpatrick.vico.core.chart.values.ChartValues
import com.patrykandpatrick.vico.core.chart.values.ChartValuesProvider
import com.patrykandpatrick.vico.core.component.Component
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.cornered.MarkerCorneredShape
import com.patrykandpatrick.vico.core.component.text.TextComponent
import com.patrykandpatrick.vico.core.component.text.VerticalPosition
import com.patrykandpatrick.vico.core.context.DrawContext
import com.patrykandpatrick.vico.core.context.MeasureContext
import com.patrykandpatrick.vico.core.extension.ceil
import com.patrykandpatrick.vico.core.extension.doubled
import com.patrykandpatrick.vico.core.extension.half
import com.patrykandpatrick.vico.core.extension.orZero
import com.patrykandpatrick.vico.core.marker.Marker
import com.patrykandpatrick.vico.core.marker.MarkerLabelFormatter

open class CustomMarkerComponent(
    private val label: TextComponent,
    private val labelXString: List<String>,
    private val indicator: Component?,
    private val guideline: LineComponent?,
) : Marker {

    private val tempBounds = RectF()

    private val TextComponent.tickSizeDp: Float
        get() = ((background as? ShapeComponent)?.shape as? MarkerCorneredShape)?.tickSizeDp.orZero

    /**
     * The indicator size (in dp).
     */
    var indicatorSizeDp: Float = 0f

    /**
     * An optional lambda function that allows for applying the color associated with a given data entry to a
     * [Component].
     */
    var onApplyEntryColor: ((entryColor: Int) -> Unit)? = null

    /**
     * The [MarkerLabelFormatter] for this marker.
     */
//    var labelFormatter: MarkerLabelFormatter = DefaultMarkerLabelFormatter()

    override fun draw(
        context: DrawContext,
        bounds: RectF,
        markedEntries: List<Marker.EntryModel>,
        chartValuesProvider: ChartValuesProvider,
    ): Unit = with(context) {
        drawGuideline(context, bounds, markedEntries)
        val halfIndicatorSize = indicatorSizeDp.half.pixels

        markedEntries.forEachIndexed { _, model ->
            onApplyEntryColor?.invoke(model.color)
            indicator?.draw(
                context,
                model.location.x - halfIndicatorSize,
                model.location.y - halfIndicatorSize,
                model.location.x + halfIndicatorSize,
                model.location.y + halfIndicatorSize,
            )
        }
        drawLabel(context, bounds, markedEntries, chartValuesProvider.getChartValues())
    }

    private fun drawLabel(
        context: DrawContext,
        bounds: RectF,
        markedEntries: List<Marker.EntryModel>,
        chartValues: ChartValues,
    ): Unit = with(context) {
        val text = markedEntries.joinToString("\n") { entryModel ->
            val value = entryModel.entry.y
            val myLabel = labelXString[entryModel.entry.x.toInt()]
            "$myLabel:\n ${formatRupiah(value.toLong())}"
        }
        val entryX = markedEntries.averageOf { it.location.x }
        val labelBounds =
            label.getTextBounds(
                context = context,
                text = text,
                width = bounds.width().toInt(),
                outRect = tempBounds
            )
        val halfOfTextWidth = labelBounds.width().half
        val x = overrideXPositionToFit(entryX, bounds, halfOfTextWidth)
        this[MarkerCorneredShape.tickXKey] = entryX

        label.drawText(
            context = context,
            text = text,
            textX = x,
            textY = bounds.top - labelBounds.height() - label.tickSizeDp.pixels,
            verticalPosition = VerticalPosition.Bottom,
            maxTextWidth = minOf(bounds.right - x, x - bounds.left).doubled.ceil.toInt(),
        )
    }

    private fun overrideXPositionToFit(
        xPosition: Float,
        bounds: RectF,
        halfOfTextWidth: Float,
    ): Float = when {
        xPosition - halfOfTextWidth < bounds.left -> bounds.left + halfOfTextWidth
        xPosition + halfOfTextWidth > bounds.right -> bounds.right - halfOfTextWidth
        else -> xPosition
    }

    private fun drawGuideline(
        context: DrawContext,
        bounds: RectF,
        markedEntries: List<Marker.EntryModel>,
    ) {
        markedEntries
            .map { it.location.x }
            .toSet()
            .forEach { x ->
                guideline?.drawVertical(
                    context,
                    bounds.top,
                    bounds.bottom,
                    x,
                )
            }
    }

    override fun getInsets(
        context: MeasureContext,
        outInsets: Insets,
        horizontalDimensions: HorizontalDimensions,
    ): Unit = with(context) {
        outInsets.top = label.getHeight(context) + label.tickSizeDp.pixels
    }
}

fun <T> Collection<T>.averageOf(selector: (T) -> Float): Float =
    fold(0f) { sum, element ->
        sum + selector(element)
    } / size