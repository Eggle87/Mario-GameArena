public class myGame {

    public static void main(String[] args)
    {
        GameArena arena = new GameArena(1920,1080);

        Mario mario = new Mario(150, 100);

        mario.addTo(arena);

        while(true)
        {
            mario.update(arena);

            arena.pause();
        }
    }
}
