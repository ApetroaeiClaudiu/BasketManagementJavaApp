import Domain.Game;

import java.io.IOException;

public interface IClient {
    void refresh(Game game) throws IOException;
}
