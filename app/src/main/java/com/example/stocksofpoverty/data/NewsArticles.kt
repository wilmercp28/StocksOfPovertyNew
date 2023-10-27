package com.example.stocksofpoverty.data


fun getNewsGenericArticles(): List<Pair<String, List<Int>>> {
    return listOf(
        Pair(
            "Local Community Comes Together for Environmental Cleanup Effort : " +
                    "In a heartwarming display of community spirit, residents joined hands for a large-scale environmental cleanup effort. The event, organized by local volunteers and businesses, resulted in cleaner parks, streets, and waterways. The collective action has garnered praise and positivity, fostering a sense of pride and unity.",
            listOf(5, 10, 15, 5, 10)
        ),
        Pair(
            "Severe Weather Causes Widespread Disruptions and Property Damage : " +
                    "A powerful storm system swept through the region, causing severe weather conditions and widespread disruptions. Homes and businesses faced significant damage, power outages were reported, and transportation networks were paralyzed. Local authorities are working tirelessly to restore normalcy, but residents remain apprehensive about the extent of the damage.",
            listOf(-15, -20, -25, -10, -5)
        )
    )
}


fun getNewsFinanceArticles(stock: Stock): List<Pair<String, List<Int>>> {
    return listOf(
        Pair(
            "Positive Economic Outlook Boosts ${stock.name} Stock : " +
                    "Optimistic economic indicators have bolstered investor confidence, leading to a surge in ${stock.name} stock prices. Analysts predict sustained growth as the market responds favorably to the promising economic conditions.",
            listOf(10, 15, 20, 10, 5)
        ),
        Pair(
            "Regulatory Changes Impact ${stock.name} Profits : " +
                    "Recent regulatory changes have affected ${stock.name}'s profits, leading to a decline in stock prices. Investors are cautious as the company navigates the evolving regulatory landscape, closely monitoring for future developments.",
            listOf(-15, -20, -25, 15, 10)
        ),
        Pair(
            "Record Profits Propel ${stock.name} to New Heights : " +
                    "Exceptional quarterly profits have propelled ${stock.name} stock to new heights. The company's strong financial performance has captured investor attention, resulting in a significant uptick in stock prices. Analysts remain bullish on the company's future prospects.",
            listOf(20, 25, 30, 20, 15)
        ),
        Pair(
            "Trade Tariff Concerns Impact Global Markets, Including ${stock.name} : " +
                    "Escalating trade tariff concerns have roiled global markets, impacting ${stock.name} stock negatively. Investors are wary of the potential disruptions to international trade, leading to a decline in stock prices. Market experts advise caution amid ongoing trade tensions.",
            listOf(-25, -30, -35, 20, 15)
        ),
        Pair(
            "${stock.name} Expands Investment Portfolio, Gains Investor Confidence : " +
                    "${stock.name} has diversified its investment portfolio, garnering confidence from investors. The strategic expansion into new sectors has been well-received, leading to a boost in stock prices. Analysts commend the company's forward-thinking approach to investments.",
            listOf(15, 20, 25, 10, 5)
        ),
        Pair(
            "Government Stimulus Package Boosts ${stock.name} and Market Sentiment : " +
                    "The government's announcement of a stimulus package has bolstered investor confidence, leading to a surge in ${stock.name} stock prices. Positive market sentiment prevails as investors anticipate economic recovery and increased consumer spending.",
            listOf(15, 10, 20, 5, 10)
        ),
        Pair(
            "Supply Chain Disruptions Impact ${stock.name} Production and Profits : " +
                    "Supply chain disruptions have hampered ${stock.name}'s production capabilities, leading to a decline in profits and stock prices. The company grapples with challenges related to material shortages and delayed shipments, prompting caution among investors.",
            listOf(-20, -15, -25, -10, -5)
        ),
        Pair(
            "Strong Q3 Earnings Report Drives ${stock.name} Stock to All-Time Highs : " +
                    "A stellar third-quarter earnings report has propelled ${stock.name} stock to record highs. Exceptional financial performance, coupled with cost-saving measures, has garnered investor confidence. Analysts commend the company's resilience amid challenging market conditions.",
            listOf(25, 30, 35, 20, 15)
        ),
        Pair(
            "Global Economic Uncertainty Dampens Investor Enthusiasm, Including ${stock.name} : " +
                    "Rising global economic uncertainty has dampened investor enthusiasm, leading to a decline in ${stock.name} stock prices. Trade tensions, geopolitical concerns, and fluctuating commodity prices contribute to a cautious market atmosphere. Investors closely monitor developments for signs of stability.",
            listOf(-30, -35, -40, -25, -20)
        ),
        Pair(
            "Strategic Partnership Enhances ${stock.name}'s Market Presence and Investor Appeal : " +
                    "${stock.name} has entered into a strategic partnership, enhancing its market presence and investor appeal. The collaboration with a key industry player has ignited optimism, resulting in a notable increase in stock prices. Market analysts highlight the potential for long-term growth and synergy.",
            listOf(20, 25, 30, 15, 10)
        )

    )
}

fun getNewsPharmaArticles(stock: Stock): List<Pair<String, List<Int>>> {
    return listOf(
        Pair(
            "Breakthrough Drug Approval Boosts ${stock.name} Stock : " +
                    "The FDA approval of a groundbreaking pharmaceutical drug has significantly boosted ${stock.name} stock prices. The new medication, hailed as a medical breakthrough, has garnered immense market interest, leading to a substantial increase in stock values.",
            listOf(15, 10, 20, 10, 5)
        ),
        Pair(
            "Clinical Trial Setbacks Impact ${stock.name} Profits and Investor Confidence : " +
                    "Setbacks in key clinical trials have impacted ${stock.name}'s profits, leading to a decline in stock prices. Investors express concerns about delays in product development, prompting caution in the pharmaceutical market. Market sentiment remains uncertain.",
            listOf(-20, -15, -25, -10, -5)
        ),
        Pair(
            "Positive Phase III Trial Results Propel ${stock.name} to New Heights : " +
                    "Positive results from a Phase III clinical trial have propelled ${stock.name} stock to new heights. The experimental drug demonstrated remarkable efficacy and safety, leading to a remarkable increase in stock prices. Analysts are optimistic about the drug's market potential.",
            listOf(25, 30, 35, 20, 15)
        ),
        Pair(
            "Supply Chain Disruptions Hamper ${stock.name} Operations and Financial Performance : " +
                    "Disruptions in the pharmaceutical supply chain have hampered ${stock.name}'s operations, leading to a decline in financial performance and stock prices. The company faces challenges related to raw material shortages and production delays, prompting concerns among investors.",
            listOf(-30, -35, -40, -25, -20)
        ),
        Pair(
            "FDA Fast-Tracks ${stock.name}'s Promising Cancer Treatment : " +
                    "The FDA's decision to fast-track ${stock.name}'s experimental cancer treatment has generated positive market buzz. The expedited regulatory process reflects the treatment's potential, resulting in a notable increase in stock prices. Investors anticipate accelerated market entry.",
            listOf(20, 25, 30, 15, 10)
        ),
        Pair(
            "Patent Expiry and Generic Competition Impact ${stock.name}'s Revenue : " +
                    "The expiry of a key pharmaceutical patent has opened doors for generic competition, impacting ${stock.name}'s revenue and leading to a decline in stock prices. The company navigates the challenge of market saturation, prompting strategic shifts in product offerings.",
            listOf(-25, -30, -35, -20, -15)
        ),
        Pair(
            "Collaboration with Research Institute Spurs ${stock.name} Innovation : " +
                    "A collaboration between ${stock.name} and a renowned research institute has spurred innovation in pharmaceutical research. The partnership's fruitful developments have led to a substantial increase in stock prices. Analysts applaud the companies' synergistic approach to advancing medical science.",
            listOf(30, 35, 40, 25, 20)
        ),
        Pair(
            "FDA Rejects ${stock.name}'s New Drug Application, Dampening Investor Enthusiasm : " +
                    "The FDA's rejection of ${stock.name}'s new drug application has dampened investor enthusiasm, leading to a decline in stock prices. The company faces regulatory hurdles, prompting uncertainty about future product launches. Investors await the company's response to the FDA's feedback.",
            listOf(-35, -40, -45, -30, -25)
        ),
        Pair(
            "Breakthrough in Rare Disease Treatment Elevates ${stock.name} Market Position : " +
                    "A breakthrough in rare disease treatment has elevated ${stock.name}'s market position. The successful development of a specialized medication has resulted in a remarkable increase in stock prices. Investors recognize the company's commitment to addressing unmet medical needs.",
            listOf(35, 40, 45, 30, 25)
        ),
        Pair(
            "Global Expansion Plans Unveiled by ${stock.name}, Fueled by Strong Earnings : " +
                    "${stock.name} has unveiled ambitious global expansion plans, fueled by strong earnings and market demand. The company's strategic vision and financial performance have generated investor confidence, resulting in a notable increase in stock prices. Analysts anticipate growth in international markets.",
            listOf(25, 30, 35, 20, 15)
        )
    )
}

fun getNewsEnergyArticles(stock: Stock): List<Pair<String, List<Int>>> {
    return listOf(
        Pair(
            "Renewable Energy Initiatives Surge, Boosting ${stock.name} Stock : " +
                    "Widespread adoption of renewable energy initiatives has led to a surge in demand for ${stock.name} products and services. The company's focus on clean energy solutions is met with enthusiasm from investors, resulting in a substantial increase in stock prices.",
            listOf(15, 10, 20, 10, 5)
        ),
        Pair(
            "Oil Price Volatility Impacts ${stock.name} Profits and Market Sentiment : " +
                    "Fluctuations in oil prices have impacted ${stock.name}'s profits, leading to a decline in stock prices. Market sentiment remains uncertain as energy companies navigate the challenges posed by unpredictable oil markets, prompting caution among investors.",
            listOf(-20, -15, -25, -10, -5)
        ),
        Pair(
            "Breakthrough in Solar Technology Propels ${stock.name} to New Heights : " +
                    "A groundbreaking advancement in solar technology has propelled ${stock.name} stock to unprecedented heights. The company's innovative solar solutions have garnered significant attention, leading to a remarkable increase in stock prices. Analysts laud the company's pioneering approach to renewable energy.",
            listOf(25, 30, 35, 20, 15)
        ),
        Pair(
            "Natural Gas Supply Shortage Impacts ${stock.name} Operations and Profits : " +
                    "A shortage in natural gas supply has hampered ${stock.name}'s operations, leading to a decline in profits and stock prices. The company grapples with challenges related to supply chain disruptions and increased demand, prompting concerns among investors.",
            listOf(-30, -35, -40, -25, -20)
        ),
        Pair(
            "Government Grants Subsidies to ${stock.name} for Clean Energy Projects : " +
                    "Government subsidies and grants have been awarded to ${stock.name} for its clean energy projects. The financial support enhances the company's ability to invest in sustainable initiatives, resulting in a notable increase in stock prices. Investors applaud the company's commitment to environmental responsibility.",
            listOf(20, 25, 30, 15, 10)
        ),
        Pair(
            "Energy Market Downturn Leads to ${stock.name} Layoffs and Cost-Cutting Measures : " +
                    "A downturn in the energy market has forced ${stock.name} to implement layoffs and cost-cutting measures. The company faces challenges related to reduced demand and pricing pressures, resulting in a decrease in stock prices. Investors express concern over the industry's economic outlook.",
            listOf(-25, -30, -35, -20, -15)
        ),
        Pair(
            "Advancements in Wind Energy Technology Drive ${stock.name} Growth : " +
                    "Significant advancements in wind energy technology have driven ${stock.name} growth. The company's investments in innovative wind power solutions have paid off, leading to a substantial increase in stock prices. Market analysts highlight the potential for further expansion and market dominance.",
            listOf(30, 35, 40, 25, 20)
        ),
        Pair(
            "Nuclear Energy Regulatory Approvals Delayed, Impacting ${stock.name} Expansion Plans : " +
                    "Delays in regulatory approvals for nuclear energy projects have impacted ${stock.name}'s expansion plans. The company faces setbacks in its efforts to expand its nuclear energy portfolio, leading to a decline in stock prices. Investors monitor regulatory developments closely for signs of progress.",
            listOf(-35, -40, -45, -30, -25)
        ),
        Pair(
            "Investor Confidence Soars as ${stock.name} Achieves Record Wind Energy Output : " +
                    "Record wind energy output has boosted investor confidence in ${stock.name}. The company's efficient wind energy projects have resulted in substantial gains, leading to a remarkable increase in stock prices. Analysts praise the company's operational excellence and sustainable energy practices.",
            listOf(35, 40, 45, 30, 25)
        ),
        Pair(
            "Energy Storage Breakthrough Enhances ${stock.name} Market Position : " +
                    "A breakthrough in energy storage technology has enhanced ${stock.name}'s market position. The company's cutting-edge energy storage solutions offer efficiency and reliability, leading to a notable increase in stock prices. Investors recognize the strategic advantage gained by ${stock.name} in the competitive energy storage market.",
            listOf(25, 30, 35, 20, 15)
        )
    )
}


fun getNewsTechArticles(stock: Stock): List<Pair<String, List<Int>>> {
    return listOf(
        Pair(
            "${stock.name}. Launches AI-Powered Smart Home Device : " +
                    "${stock.name}. has introduced a state-of-the-art AI-powered smart home device. Industry analysts predict a surge in demand as consumers eagerly adopt this innovative technology.",
            listOf(25, 15, 25, -15, -25)
        ),
        Pair(
            "${stock.name} Announces Expansion into AI Research : " +
                    "${stock.name}, a prominent tech giant, has announced a strategic expansion into artificial intelligence research. The company's commitment to AI innovation is met with investor enthusiasm, resulting in increased demand for ${stock.name} stocks.",
            listOf(20, 15, -10, 25, -20)
        ),
        Pair(
            "${stock.name} Launches AI-Driven Virtual Assistant : " +
                    "TechGenius has introduced a state-of-the-art AI-powered virtual assistant, revolutionizing how people interact with technology. The innovative virtual assistant, equipped with advanced natural language processing capabilities, is set to transform user experiences across various applications. Experts predict a surge in demand as consumers eagerly adopt this cutting-edge technology.",
            listOf(5, 10, 15, -10, -5)
        ),
        Pair(
            "${stock.name} Announces Breakthrough in Nanotechnology : " +
                    "NanoTech Industries, a pioneering tech firm, has announced a significant breakthrough in nanotechnology research. The company's innovation holds the promise of revolutionizing industries, from electronics to medicine, by enabling precise manipulation at the atomic level. Investors are showing enthusiastic support, reflecting the immense potential of NanoTech Industries' discovery.",
            listOf(10, 15, 20, -15, -10)
        ),
        Pair(
            "${stock.name} Expands into Quantum Computing Research : " +
                    "QuantumSoft, a prominent tech giant, has revealed its strategic expansion into quantum computing research. Quantum computing, with its ability to handle complex computations, holds the key to solving problems previously deemed unsolvable. The company's commitment to advancing the field is met with excitement from both the scientific community and investors.",
            listOf(15, 20, 25, -20, -15)
        ),
        Pair(
            "${stock.name} Launches Kotlin Compose Plugin for Streamlined Development : " +
                    "ComposeHub, a tech platform dedicated to Kotlin Compose, has launched a powerful plugin aimed at streamlining the development process. This toolset simplifies complex tasks, enabling developers to focus on creativity and innovation, further accelerating Kotlin Compose adoption. The developer community hails this move as a game-changer for modern app development.",
            listOf(20, 25, 30, -25, -20)
        ),
        Pair(
            "${stock.name} Embraces Kotlin Compose for Next-Gen User Experiences : " +
                    "InteractiveApps, a forward-thinking app development studio, has fully embraced Kotlin Compose for creating next-generation user experiences. By leveraging the expressive power of Kotlin Compose, InteractiveApps is reshaping how users interact with mobile applications, setting new industry standards. Early adopters praise the studio's innovative approach to app design.",
            listOf(25, 30, 30, -30, -25)
        ),
        Pair(
            "${stock.name} Launches AI-Driven Virtual Assistant : " +
                    "TechGenius has introduced a state-of-the-art AI-powered virtual assistant, revolutionizing how people interact with technology. The innovative virtual assistant, equipped with advanced natural language processing capabilities, is set to transform user experiences across various applications. Experts predict a surge in demand as consumers eagerly adopt this cutting-edge technology.",
            listOf(5, 10, 15, -10, -5)
        ),
        Pair(
            "${stock.name} Faces Legal Challenges Over Patent Dispute : " +
                    "Recent legal challenges have emerged for ${stock.name} regarding a patent dispute. Legal experts are closely monitoring the situation, and investors have expressed concerns about the potential financial implications for the company.",
            listOf(-10, -15, -20, 15, 10)
        ),
        Pair(
            "${stock.name} Expands into Quantum Computing Research : " +
                    "QuantumSoft, a prominent tech giant, has revealed its strategic expansion into quantum computing research. Quantum computing, with its ability to handle complex computations, holds the key to solving problems previously deemed unsolvable. The company's commitment to advancing the field is met with excitement from both the scientific community and investors.",
            listOf(15, 20, -25, -20, 10)
        ),
        Pair(
            "${stock.name} Reports Lower-Than-Expected Quarterly Earnings : " +
                    "${stock.name} has released its quarterly financial report, indicating lower-than-expected earnings. Analysts attribute the decline to market fluctuations and increased competition. Investors are closely watching how the company plans to address these challenges.",
            listOf(-20, -25, 30, -15, -10)
        ),
        Pair(
            "${stock.name} Faces Security Breach, Customer Data Compromised : " +
                    "In a recent security breach, ${stock.name} experienced a data breach compromising customer information. The company is actively investigating the incident and taking measures to enhance cyber security. Investors have reacted cautiously, leading to a dip in stock prices.",
            listOf(-25, -30, -35, 20, 15)
        )
    )
}