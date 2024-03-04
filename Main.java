public class myGame {

    public static void main(String[] args)
    {
        GameArena arena = new GameArena(500,500);

        Mario mario = new Mario(150, 0);

        mario.addTo(arena);

        while(true)
        {
            mario.update(arena);

            arena.pause();
        }
    }
}
