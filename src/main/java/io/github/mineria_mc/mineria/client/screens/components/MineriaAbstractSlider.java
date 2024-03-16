package io.github.mineria_mc.mineria.client.screens.components;

import java.util.function.Consumer;

import com.ibm.icu.text.DecimalFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

@SuppressWarnings("unused")
public class MineriaAbstractSlider extends AbstractSliderButton {
    protected Component prefix;
    protected Component suffix;

    protected double minValue;
    protected double maxValue;

    protected double stepSize;
    protected boolean drawString;

    private final DecimalFormat format;

    private final Consumer<MineriaAbstractSlider> action;

    public MineriaAbstractSlider(int x, int y, int width, int height, Component prefix, Component suffix,
            double minValue, double maxValue, double currentValue, double stepSize, int precision, boolean drawString, Consumer<MineriaAbstractSlider> action) {
        super(x, y, width, height, Component.empty(), 0d);
        this.prefix = prefix;
        this.suffix = suffix;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.stepSize = Math.abs(stepSize);
        this.value = this.snapToNearest((currentValue - minValue) / (maxValue - minValue));
        this.drawString = drawString;

        if (stepSize == 0D) {
            precision = Math.min(precision, 4);

            StringBuilder builder = new StringBuilder("0");

            if (precision > 0)
                builder.append('.');

            while (precision-- > 0)
                builder.append('0');

            this.format = new DecimalFormat(builder.toString());
        } else if (Mth.equal(this.stepSize, Math.floor(this.stepSize))) {
            this.format = new DecimalFormat("0");
        } else {
            this.format = new DecimalFormat(Double.toString(this.stepSize).replaceAll("\\d", "0"));
        }
        this.action = action;

        this.updateMessage();
    }

    public MineriaAbstractSlider(int x, int y, int width, int height, Component prefix, Component suffix,
            double minValue, double maxValue, double currentValue, boolean drawString, Consumer<MineriaAbstractSlider> action) {
        this(x, y, width, height, prefix, suffix, minValue, maxValue, currentValue, 1D, 0, drawString, action);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.setValueFromMouse(mouseX);
    }

    private void setValueFromMouse(double mouseX) {
        this.setSliderValue((mouseX - (this.getX() + 4) / (this.width - 8)));
    }

    private void setSliderValue(double value) {
        double oldValue = this.value;
        this.value = this.snapToNearest(value);
        if (!Mth.equal(oldValue, this.value))
            this.applyValue();

        this.updateMessage();
    }

    public double getValue() {
        return this.value * (maxValue - minValue) + minValue;
    }

    public long getValueLong() {
        return Math.round(this.getValue());
    }

    public int getValueInt() {
        return (int) this.getValueLong();
    }

    public void setValue(double value) {
        this.value = this.snapToNearest((value - this.minValue) / (this.maxValue - this.minValue));
        this.updateMessage();
    }

    public String getValueString() {
        return this.format.format(this.getValue());
    }

    @Override
    protected void updateMessage() {
        if (this.drawString) {
            this.setMessage(Component.literal("").append(prefix).append(this.getValueString()).append(suffix));
        } else {
            this.setMessage(Component.empty());
        }
    }

    @Override
    protected void applyValue() {}

    private double snapToNearest(double value) {
        if (stepSize <= 0D)
            return Mth.clamp(value, 0D, 1D);

        value = Mth.lerp(Mth.clamp(value, 0D, 1D), this.minValue, this.maxValue);

        value = (stepSize * Math.round(value / stepSize));

        if (this.minValue > this.maxValue) {
            value = Mth.clamp(value, this.maxValue, this.minValue);
        } else {
            value = Mth.clamp(value, this.minValue, this.maxValue);
        }

        return Mth.map(value, this.minValue, this.maxValue, 0D, 1D);
    }
}
