package com.dr.jjsembako.core.utils.chart

import android.graphics.Typeface
import android.text.Layout
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.compose.component.overlayingComponent
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.core.chart.dimensions.HorizontalDimensions
import com.patrykandpatrick.vico.core.chart.insets.Insets
import com.patrykandpatrick.vico.core.component.shape.DashedShape
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.cornered.Corner
import com.patrykandpatrick.vico.core.component.shape.cornered.MarkerCorneredShape
import com.patrykandpatrick.vico.core.context.MeasureContext
import com.patrykandpatrick.vico.core.extension.copyColor
import com.patrykandpatrick.vico.core.marker.Marker

@Composable
internal fun rememberMarker(labelXString: List<String>): Marker {
    val labelBackgroundColor = MaterialTheme.colorScheme.surface
    val labelBackground = remember(labelBackgroundColor) {
        ShapeComponent(labelBackgroundShape, labelBackgroundColor.toArgb()).setShadow(
            radius = LABEL_BACKGROUND_SHADOW_RADIUS,
            dy = LABEL_BACKGROUND_SHADOW_DY,
            applyElevationOverlay = true,
        )
    }
    val label = if(isSystemInDarkTheme()){
        textComponent(
            background = labelBackground,
            lineCount = LABEL_LINE_COUNT,
            padding = labelPadding,
            typeface = Typeface.MONOSPACE,
            textAlignment = Layout.Alignment.ALIGN_CENTER,
            color = Color.White
        )
    } else {
        textComponent(
            background = labelBackground,
            lineCount = LABEL_LINE_COUNT,
            padding = labelPadding,
            typeface = Typeface.MONOSPACE,
            textAlignment = Layout.Alignment.ALIGN_CENTER
        )
    }
    val indicatorInnerComponent =
        shapeComponent(Shapes.pillShape, MaterialTheme.colorScheme.surface)
    val indicatorCenterComponent = shapeComponent(Shapes.pillShape, Color.White)
    val indicatorOuterComponent = shapeComponent(Shapes.pillShape, Color.White)
    val indicator = overlayingComponent(
        outer = indicatorOuterComponent,
        inner = overlayingComponent(
            outer = indicatorCenterComponent,
            inner = indicatorInnerComponent,
            innerPaddingAll = indicatorInnerAndCenterComponentPaddingValue,
        ),
        innerPaddingAll = indicatorCenterAndOuterComponentPaddingValue,
    )
    val guideline = lineComponent(
        MaterialTheme.colorScheme.onSurface.copy(GUIDELINE_ALPHA),
        guidelineThickness,
        guidelineShape,
    )
    return remember(label, indicator, guideline) {
        object : CustomMarkerComponent(label, labelXString, indicator, guideline) {
            init {
                indicatorSizeDp = INDICATOR_SIZE_DP
                onApplyEntryColor = { entryColor ->
                    indicatorOuterComponent.color =
                        entryColor.copyColor(INDICATOR_OUTER_COMPONENT_ALPHA)
                    with(indicatorCenterComponent) {
                        color = entryColor
                        setShadow(
                            radius = INDICATOR_CENTER_COMPONENT_SHADOW_RADIUS,
                            color = entryColor
                        )
                    }
                }
            }

//            override fun draw(
//                context: DrawContext,
//                bounds: RectF,
//                markedEntries: List<Marker.EntryModel>,
//                chartValuesProvider: ChartValuesProvider
//            ) {
//                super.draw(context, bounds, emptyList(), chartValuesProvider)
//
//                markedEntries.forEach { entryModel ->
//                    val value = entryModel.entry.y
//                    val myLabel = labelXString[entryModel.entry.x.toInt()]
//                    val formattedText = "$myLabel:\n ${ formatRupiah(value.toLong()) }"
//                    val labelX = entryModel.location.x
//                    val labelY = entryModel.location.y - bounds.height() / 4 // Adjust label position to be above the indicator
//                    label.drawText(context, formattedText, labelX, labelY)
//                }
//            }

            override fun getInsets(
                context: MeasureContext,
                outInsets: Insets,
                horizontalDimensions: HorizontalDimensions,
            ) = with(context) {
                outInsets.top = label.getHeight(context) + labelBackgroundShape.tickSizeDp.pixels +
                        LABEL_BACKGROUND_SHADOW_RADIUS.pixels * SHADOW_RADIUS_MULTIPLIER -
                        LABEL_BACKGROUND_SHADOW_DY.pixels
            }
        }
    }
}

private const val LABEL_BACKGROUND_SHADOW_RADIUS = 4f
private const val LABEL_BACKGROUND_SHADOW_DY = 2f
private const val LABEL_LINE_COUNT = 2
private const val GUIDELINE_ALPHA = .2f
private const val INDICATOR_SIZE_DP = 36f
private const val INDICATOR_OUTER_COMPONENT_ALPHA = 32
private const val INDICATOR_CENTER_COMPONENT_SHADOW_RADIUS = 12f
private const val GUIDELINE_DASH_LENGTH_DP = 8f
private const val GUIDELINE_GAP_LENGTH_DP = 4f
private const val SHADOW_RADIUS_MULTIPLIER = 1.3f

private val labelBackgroundShape = MarkerCorneredShape(Corner.FullyRounded)
private val labelHorizontalPaddingValue = 8.dp
private val labelVerticalPaddingValue = 4.dp
private val labelPadding = dimensionsOf(labelHorizontalPaddingValue, labelVerticalPaddingValue)
private val indicatorInnerAndCenterComponentPaddingValue = 5.dp
private val indicatorCenterAndOuterComponentPaddingValue = 10.dp
private val guidelineThickness = 2.dp
private val guidelineShape =
    DashedShape(Shapes.pillShape, GUIDELINE_DASH_LENGTH_DP, GUIDELINE_GAP_LENGTH_DP)