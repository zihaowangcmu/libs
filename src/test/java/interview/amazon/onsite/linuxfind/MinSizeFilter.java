package interview.amazon.onsite.linuxfind;

public class MinSizeFilter implements Filter{

    int minSize;

    public MinSizeFilter(int minSize) {
        this.minSize = minSize;
    }

    @Override
    public boolean validate(MyFile file) {
        return file.getSize() >= this.minSize;
    }
}
