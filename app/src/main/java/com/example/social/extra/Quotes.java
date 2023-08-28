package com.example.social.extra;

import java.util.Random;

public class Quotes {
    private static String[] quote = new String[]{
            "”We cannot solve problems with the kind of thinking we employed when we came up with them.”\n — Albert Einstein",
            "“Learn as if you will live forever, live like you will die tomorrow.”\n — Mahatma Gandhi",
            "“When you change your thoughts, remember to also change your world.”\n—Norman Vincent Peale",
            "“It is only when we take chances, when our lives improve. The initial and the most difficult risk that we need to take is to become honest.” \n—Walter Anderson",
            "“Nature has given us all the pieces required to achieve exceptional wellness and health, but has left it to us to put these pieces together.”\n—Diane McLaren",
            "\"Success is not final; failure is not fatal: It is the courage to continue that counts.\"\n — Winston S. Churchill",
            "\"It is better to fail in originality than to succeed in imitation.\"\n — Herman Melville",
            "\"The road to success and the road to failure are almost exactly the same.\" \n— Colin R. Davis",
            "“Success usually comes to those who are too busy looking for it.” \n— Henry David Thoreau",
            "“Develop success from failures. Discouragement and failure are two of the surest stepping stones to success.” \n—Dale Carnegie",
            "“There are three ways to ultimate success: The first way is to be kind. The second way is to be kind. The third way is to be kind.” \n—Mister Rogers",
            "“Success is peace of mind, which is a direct result of self-satisfaction in knowing you made the effort to become the best of which you are capable.” \n—John Wooden",
            "“I never dreamed about success. I worked for it.” \n—Estée Lauder",
            "“Success is getting what you want, happiness is wanting what you get.” \n―W. P. Kinsella",
            "“The pessimist sees difficulty in every opportunity. The optimist sees opportunity in every difficulty.\" \n— Winston Churchill",
            "“Don’t let yesterday take up too much of today.” \n— Will Rogers",
            "“You learn more from failure than from success. Don’t let it stop you. Failure builds character.” ",
            "“If you are working on something that you really care about, you don’t have to be pushed. The vision pulls you.” \n— Steve Jobs",
            "“Experience is a hard teacher because she gives the test first, the lesson afterwards.” \n―Vernon Sanders Law",
            "“To know how much there is to know is the beginning of learning to live.” \n—Dorothy West",
            "“Goal setting is the secret to a compelling future.” \n— Tony Robbins",
            "“Concentrate all your thoughts upon the work in hand. The sun's rays do not burn until brought to a focus. “ \n— Alexander Graham Bell",
            "“Either you run the day or the day runs you.” \n— Jim Rohn",
            "“I’m a greater believer in luck, and I find the harder I work the more I have of it.” \n— Thomas Jefferson",
            "“When we strive to become better than we are, everything around us becomes better too.” \n— Paulo Coelho",
            "“Opportunity is missed by most people because it is dressed in overalls and looks like work.” \n— Thomas Edison",
            "“Setting goals is the first step in turning the invisible into the visible.” \n— Tony Robbins"
    };

    public static String getRandomQuote() {
        Random random = new Random();
        int randomIndex = random.nextInt(quote.length);
        return quote[randomIndex];
    }
}
