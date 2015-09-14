package it.playfellas.superapp.ui.slave.game1;

/**
 * Created by affo on 14/09/15.
 */
public class SlaveGame1ColorAgainFragment extends SlaveGame1ColorFragment {
    @Override
    protected void swapBackground(boolean isInverted) {
        super.swapBackground(isInverted);
        getConveyorDown().toggleGreyscale();
        getConveyorUp().toggleGreyscale();
    }
}
