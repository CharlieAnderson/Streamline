package com.example.charlesanderson.streamline;

import com.brandongogetap.stickyheaders.exposed.StickyHeader;

/**
 * Created by charlesanderson on 3/9/17.
 */

public class HeaderItem extends TaskItem implements StickyHeader {
    private final String title;
    private final Section section;

    public HeaderItem(Section section) {
        this.section = section;
        this.title = section.toString();
    }

    public String getTitle() {
        return title;
    }

    public Section getSection() {
        return section;
    }
}
