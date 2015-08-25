package gov.multiversal.x11colors;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Dave Small on 3/26/15.
 * - a representative subset of X11 colors
 */
public class X11Color {
    static public final String[] NAME = new String[] {
            "Alice Blue", "Antique White", "Aqua",
            "Aquamarine", "Azure", "Beige", "Bisque", "Black", "Blanched Almond",
            "Blue", "Blue Violet", "Brown", "Burlywood", "Cadet Blue", "Chartreuse",
            "Chocolate", "Coral", "Cornflower", "Cornsilk", "Crimson", "Cyan",
            "Dark Blue", "Dark Cyan", "Dark Goldenrod", "Dark Gray", "Dark Green",
            "Dark Khaki", "Dark Magenta", "Dark Olive Green", "Dark Orange",
            "Dark Orchid", "Dark Red", "Dark Salmon", "Dark Sea Green",
            "Dark Slate Blue", "Dark Slate Gray", "Dark Turquoise", "Dark Violet",
            "Deep Pink", "Deep Sky Blue", "Dim Gray", "Dodger Blue", "Firebrick",
            "Floral White", "Forest Green", "Fuchsia", "Gainsboro", "Ghost White",
            "Gold", "Goldenrod", "Gray", "Web Gray", "Green", "Web Green",
            "Green Yellow", "Honeydew", "Hot Pink", "Indian Red", "Indigo",
            "Ivory", "Khaki", "Lavender", "Lavender Blush", "Lawn Green",
            "Lemon Chiffon", "Light Blue", "Light Coral", "Light Cyan",
            "Light Goldenrod", "Light Gray", "Light Green", "Light Pink",
            "Light Salmon", "Light Sea Green", "Light Sky Blue", "Light Slate Gray",
            "Light Steel Blue", "Light Yellow", "Lime", "Lime Green", "Linen", "Magenta",
            "Maroon", "Web Maroon", "Medium Aquamarine", "Medium Blue", "Medium Orchid",
            "Medium Purple", "Medium Sea Green", "Medium Slate Blue",
            "Medium Spring Green", "Medium Turquoise", "Medium Violet Red",
            "Midnight Blue", "Mint Cream", "Misty Rose", "Moccasin", "Navajo White",
            "Navy Blue", "Old Lace", "Olive", "Olive Drab", "Orange", "Orange Red",
            "Orchid", "Pale Goldenrod", "Pale Green", "Pale Turquoise", "Pale Violet Red",
            "Papaya Whip", "Peach Puff", "Peru", "Pink", "Plum", "Powder Blue", "Purple",
            "Web Purple", "Rebecca Purple", "Red", "Rosy Brown", "Royal Blue",
            "Saddle Brown", "Salmon", "Sandy Brown", "Sea Green", "Seashell", "Sienna",
            "Silver", "Sky Blue", "Slate Blue", "Slate Gray", "Snow", "Spring Green",
            "Steel Blue", "Tan", "Teal", "Thistle", "Tomato", "Turquoise", "Violet",
            "Wheat", "White", "White Smoke", "Yellow", "Yellow Green"
    };

    static public final String[] RGBHEX = new String[] {
            "#F0F8FF", "#FAEBD7", "#00FFFF", "#7FFFD4", "#F0FFFF",
            "#F5F5DC", "#FFE4C4", "#000000", "#FFEBCD", "#0000FF",
            "#8A2BE2", "#A52A2A", "#DEB887", "#5F9EA0", "#7FFF00",
            "#D2691E", "#FF7F50", "#6495ED", "#FFF8DC", "#DC143C",
            "#00FFFF", "#00008B", "#008B8B", "#B8860B", "#A9A9A9",
            "#006400", "#BDB76B", "#8B008B", "#556B2F", "#FF8C00",
            "#9932CC", "#8B0000", "#E9967A", "#8FBC8F", "#483D8B",
            "#2F4F4F", "#00CED1", "#9400D3", "#FF1493", "#00BFFF",
            "#696969", "#1E90FF", "#B22222", "#FFFAF0", "#228B22",
            "#FF00FF", "#DCDCDC", "#F8F8FF", "#FFD700", "#DAA520",
            "#BEBEBE", "#808080", "#00FF00", "#008000", "#ADFF2F",
            "#F0FFF0", "#FF69B4", "#CD5C5C", "#4B0082", "#FFFFF0",
            "#F0E68C", "#E6E6FA", "#FFF0F5", "#7CFC00", "#FFFACD",
            "#ADD8E6", "#F08080", "#E0FFFF", "#FAFAD2", "#D3D3D3",
            "#90EE90", "#FFB6C1", "#FFA07A", "#20B2AA", "#87CEFA",
            "#778899", "#B0C4DE", "#FFFFE0", "#00FF00", "#32CD32",
            "#FAF0E6", "#FF00FF", "#B03060", "#7F0000", "#66CDAA",
            "#0000CD", "#BA55D3", "#9370DB", "#3CB371", "#7B68EE",
            "#00FA9A", "#48D1CC", "#C71585", "#191970", "#F5FFFA",
            "#FFE4E1", "#FFE4B5", "#FFDEAD", "#000080", "#FDF5E6",
            "#808000", "#6B8E23", "#FFA500", "#FF4500", "#DA70D6",
            "#EEE8AA", "#98FB98", "#AFEEEE", "#DB7093", "#FFEFD5",
            "#FFDAB9", "#CD853F", "#FFC0CB", "#DDA0DD", "#B0E0E6",
            "#A020F0", "#7F007F", "#663399", "#FF0000", "#BC8F8F",
            "#4169E1", "#8B4513", "#FA8072", "#F4A460", "#2E8B57",
            "#FFF5EE", "#A0522D", "#C0C0C0", "#87CEEB", "#6A5ACD",
            "#708090", "#FFFAFA", "#00FF7F", "#4682B4", "#D2B48C",
            "#008080", "#D8BFD8", "#FF6347", "#40E0D0", "#EE82EE",
            "#F5DEB3", "#FFFFFF", "#F5F5F5", "#FFFF00", "#9ACD32"
    };

    static public final int COUNT = (NAME.length == RGBHEX.length) ? NAME.length : -1;

    static public List<X11Color> colors = X11Color.makeColorList();

    private final String name;
    private final String hexcode;

    // class level

    static private List<X11Color> makeColorList() {
        if ( NAME.length < 0 ) throw new Error( "ERROR: internal data are inconsistent" );

        ArrayList<X11Color> list = new ArrayList<X11Color>( COUNT );
        for ( int i = 0; i != COUNT; ++i )
            list.add( new X11Color( i ) );

        return Collections.unmodifiableList( list );
    }

    static public X11Color getColor( int i ) {
        return colors.get( i );
    }

    // instance level

    private X11Color(int i) {
        name = NAME[i];
        hexcode = RGBHEX[i];
    }

    public String getName() {
        return name;
    }

    public String getHexcode() {
        return hexcode;
    }

    public int getColorAsInt() {
        return Color.parseColor( hexcode );
    }
}
