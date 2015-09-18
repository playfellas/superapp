package it.playfellas.superapp.ui.slave.game3;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.widget.ImageView;

import org.apache.commons.lang3.ArrayUtils;

import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.R;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.tiles.TileSize;
import it.playfellas.superapp.ui.BitmapUtils;

/**
 * Created by Stefano Cappa on 03/09/15.
 */
public class Slave3Utils {

    public static TileSize[] getTileSizes() {
        TileSize[] sizes = TileSize.values();
        ArrayUtils.reverse(sizes);
        return sizes;
    }

    public static void updateImageViewsTower(Tile[] tiles, ImageView[] imageViewsArray, Resources resources) {
        int color;
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] != null) {
                //parse color requires this format "#AARRGGBB" or "#RRGGBB"
                color = Color.parseColor(tiles[i].getColor().hex());
                imageViewsArray[i].setImageBitmap(
                        BitmapUtils.scaleInsideWithFrame(
                                getColoredBitmapFromTile(tiles[i], resources, color),
                                getTileSizes()[i].getMultiplier(),
                                Color.TRANSPARENT
                        )
                );
            } else {
                //if null add a transparent image
                imageViewsArray[i].setImageBitmap(getBitmapFromId(R.drawable._trasparente, resources));
            }
        }
    }

    private static Bitmap getColoredBitmapFromTile(Tile t, Resources resources, int color) {
        return BitmapUtils.overlayColor(getBitmapFromTile(t, resources), color);
    }

    private static Bitmap getBitmapFromTile(Tile t, Resources resources) {
        int resId = resources.getIdentifier(t.getName(), InternalConfig.DRAWABLE_RESOURCE, InternalConfig.PACKAGE_NAME);
        return getBitmapFromId(resId, resources);
    }

    private static Bitmap getBitmapFromId(int resId, Resources resources) {
        return BitmapFactory.decodeResource(resources, resId);
    }
}
