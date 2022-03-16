package interview.amazon.onsite.linuxfind;

import java.util.Arrays;
import java.util.List;

public class OrFilter implements Filter{

    List<Filter> filters;

    public OrFilter(Filter... filters) {
        this.filters.addAll(Arrays.asList(filters));
    }
    @Override
    public boolean validate(MyFile file) {
        for (Filter filter : filters) {
            if (filter.validate(file)) return true;
        }
        return false;
    }
}
