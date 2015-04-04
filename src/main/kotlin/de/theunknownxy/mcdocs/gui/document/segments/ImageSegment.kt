package de.theunknownxy.mcdocs.gui.document.segments

import de.theunknownxy.mcdocs.docs.ImageBlock
import de.theunknownxy.mcdocs.gui.document.Document
import de.theunknownxy.mcdocs.utils.GuiUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

class ImageSegment(document: Document, private val elem: ImageBlock) : Segment(document) {
    companion object {
        private val PADDING_BOTTOM = 3
    }

    private var img_width = 0
    private var img_height = 0

    private var resource_loc: ResourceLocation? = null
    private var dynamic_tex: DynamicTexture? = null
    private var lazy_init = false

    private fun lazy_init() {
        if (!lazy_init) {
            if (elem.src.contains(":")) {
                resource_loc = ResourceLocation(elem.src)
                img_width = elem.width
                img_height = elem.height
            } else {
                //TODO: Store the data dir somewhere else
                val mcdocs_dir = File(Minecraft.getMinecraft().mcDataDir, "doc")
                val img_path = File(mcdocs_dir, elem.src)
                try {
                    val buftex = ImageIO.read(img_path)
                    dynamic_tex = DynamicTexture(buftex)
                    resource_loc = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(elem.src, dynamic_tex)
                    // Scale u/v to 256x256
                    img_width = (elem.width.toFloat() / buftex.getWidth() * 256).toInt()
                    img_height = (elem.height.toFloat() / buftex.getHeight() * 256).toInt()
                } catch(e: IOException) {
                    println("Cannot read image from path '$img_path'")
                }
            }

            lazy_init = true
        }
    }

    override val height: Float
        get() {
            val ratio = elem.height / elem.width.toFloat()
            return ratio * width.toFloat() + PADDING_BOTTOM
        }

    override fun draw() {
        lazy_init()

        val resloc = resource_loc
        if (resloc != null) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(resloc)
            GL11.glColor3f(1f, 1f, 1f)
            GuiUtils.drawTexturedModalRect(x.toInt(), y.toInt(), 0.toDouble(), 0, 0, width.toInt(), height.toInt() - PADDING_BOTTOM, img_width, img_height)
        }
    }
}