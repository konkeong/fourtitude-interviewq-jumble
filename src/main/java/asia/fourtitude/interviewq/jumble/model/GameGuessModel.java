package asia.fourtitude.interviewq.jumble.model;

import java.util.Date;

import asia.fourtitude.interviewq.jumble.core.GameState;

public class GameGuessModel {

    private String id;

    private Date createdAt;

    private Date modifiedAt;

    private GameState gameState;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (id != null) {
            sb.append(sb.length() == 0 ? "" : ", ").append("id=[").append(id).append(']');
        }
        if (createdAt != null) {
            sb.append(sb.length() == 0 ? "" : ", ").append("createdAt=[").append(createdAt.toInstant()).append(']');
        }
        if (modifiedAt != null) {
            sb.append(sb.length() == 0 ? "" : ", ").append("modifiedAt=[").append(modifiedAt.toInstant()).append(']');
        }
        if (gameState != null) {
            sb.append(sb.length() == 0 ? "" : ", ").append("gameState=[").append(gameState).append(']');
        }
        return sb.toString();
    }

}
