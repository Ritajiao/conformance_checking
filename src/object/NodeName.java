package object;

//对应petri网的name节点，含有文本和坐标,方便前端绘制图形
public class NodeName {
    private String text;
    private int offsetX;
    private int offsetY;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }
}
