package love.yinlin.libpag.example

/**
 *
 * Created by @juliswang on 2025/08/28 15:21
 *
 * @Description
 */
// 使用枚举类定义 TagCode（假设 pag 是一个对象或类）
enum class TagCode(val value: Int) {
    End(0),
    FontTables(1),
    VectorCompositionBlock(2),
    CompositionAttributes(3),
    ImageTables(4),
    LayerBlock(5),
    LayerAttributes(6),
    SolidColor(7),
    TextSource(8),
    TextMoreOption(10),
    ImageReference(11),
    CompositionReference(12),
    Transform2D(13),
    MaskBlock(14),
    ShapeGroup(15),
    Rectangle(16),
    Ellipse(17),
    PolyStar(18),
    ShapePath(19),
    Fill(20),
    Stroke(21),
    GradientFill(22),
    GradientStroke(23),
    MergePaths(24),
    TrimPaths(25),
    Repeater(26),
    RoundCorners(27),
    Performance(28),
    DropShadowStyle(29),
    CachePolicy(30),
    FileAttributes(31),
    TimeStretchMode(32),
    Mp4Header(33),

    // 34 ~ 44 are preserved (这些值在 Kotlin 中可以跳过或添加占位符注释)

    BitmapCompositionBlock(45),
    BitmapSequence(46),
    ImageBytes(47),
    ImageBytesV2(48),  // with scaleFactor
    ImageBytesV3(49),  // width transparent border stripped

    VideoCompositionBlock(50),
    VideoSequence(51),

    LayerAttributesV2(52),
    MarkerList(53),
    ImageFillRule(54),
    AudioBytes(55),
    MotionTileEffect(56),
    LevelsIndividualEffect(57),
    CornerPinEffect(58),
    BulgeEffect(59),
    FastBlurEffect(60),
    GlowEffect(61),

    LayerAttributesV3(62),
    LayerAttributesExtra(63),

    TextSourceV2(64),

    DropShadowStyleV2(65),
    DisplacementMapEffect(66),
    ImageFillRuleV2(67),

    TextSourceV3(68),
    TextPathOption(69),

    TextAnimator(70),
    TextRangeSelector(71),
    TextAnimatorPropertiesTrackingType(72),
    TextAnimatorPropertiesTrackingAmount(73),
    TextAnimatorPropertiesFillColor(74),
    TextAnimatorPropertiesStrokeColor(75),
    TextAnimatorPropertiesPosition(76),
    TextAnimatorPropertiesScale(77),
    TextAnimatorPropertiesRotation(78),
    TextAnimatorPropertiesOpacity(79),
    TextWigglySelector(80),

    RadialBlurEffect(81),
    MosaicEffect(82),
    EditableIndices(83),
    MaskBlockV2(84),
    GradientOverlayStyle(85),
    BrightnessContrastEffect(86),
    HueSaturationEffect(87),

    LayerAttributesExtraV2(88),  // add PreserveAlpha
    EncryptedData(89),
    Transform3D(90),
    CameraOption(91),

    StrokeStyle(92),
    OuterGlowStyle(93),
    ImageScaleModes(94),

    // add new tags here...

    Count(95);  // 根据您的定义，Count 应该是最后一个值

    companion object {
        // 提供从整数值获取枚举的便捷方法
        fun fromValue(value: Int): TagCode? {
            return entries.find { it.value == value }
        }

        // 获取所有枚举值的列表
        fun getAllValues(): List<TagCode> {
            return entries.toList()
        }
    }
}

// 使用不可变列表存储标签码和版本的映射
val TagCodeVersionList = listOf(
    TagCode.FileAttributes to "1.0",
    TagCode.LayerAttributesExtra to "2.0",
    TagCode.MosaicEffect to "3.2",
    TagCode.GradientOverlayStyle to "4.1",
    TagCode.CameraOption to "4.2",
    TagCode.ImageScaleModes to "4.3"
)

// 将标签码转换为对应版本的函数
fun tagCodeToVersion(tagCode: Int): String {
    return TagCodeVersionList.firstOrNull { tagCode <= it.first.value }?.second ?: "Unknown"
}