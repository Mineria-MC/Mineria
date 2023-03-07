package io.github.mineria_mc.mineria.client.screens.apothecarium.page_sets;

import io.github.mineria_mc.mineria.client.screens.apothecarium.PageCreationContext;
import io.github.mineria_mc.mineria.client.screens.apothecarium.pages.ApothecariumPage;

import java.util.function.Function;

@FunctionalInterface
public interface PageSet {
    ApothecariumPage[] pages(PageCreationContext ctx);

    default PageStart pageStart() {
        return PageStart.DEFAULT;
    }

    static PageSet singleton(Function<PageCreationContext, ApothecariumPage> pageFactory) {
        return ctx -> new ApothecariumPage[] {pageFactory.apply(ctx)};
    }

    static PageSet singleton(Function<PageCreationContext, ApothecariumPage> pageFactory, PageStart start) {
        return new PageSet() {
            @Override
            public ApothecariumPage[] pages(PageCreationContext ctx) {
                return new ApothecariumPage[] {pageFactory.apply(ctx)};
            }

            @Override
            public PageStart pageStart() {
                return start;
            }
        };
    }

    enum PageStart {
        /**
         * Always start on left page.
         */
        EVEN,
        /**
         * Always start on right page.
         */
        ODD,
        /**
         * Let the pages order decide.
         */
        DEFAULT;

        public boolean validPageIndex(int pageIndex) {
            return this == DEFAULT || (this == EVEN) == (pageIndex % 2 == 0);
        }
    }
}
