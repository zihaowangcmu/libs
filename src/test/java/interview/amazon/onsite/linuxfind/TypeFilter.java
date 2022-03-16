package interview.amazon.onsite.linuxfind;

public class TypeFilter implements Filter{

    int type;

    public TypeFilter(int type) {
        this.type = type;
    }

    @Override
    public boolean validate(MyFile file) {
        return file.getType() == type;
    }
}
