## **Архитектура**

Я разбил приложение на слои для работы с логикой на разных уровнях:

**data** — взаимодействие с базой данных

**domain** — логика приложения

**presentation** — экраны приложения и то, как они реагируют на действия пользователя

На слое presentation я реализовал упрощенную архитектуру Flux. Состояние экранов хранится в LiveData и изменяется только через функцию-редьюсер (invoke во ViewModel). Потомки класса [State](https://github.com/Kondenko/FunBoxTestTask/blob/master/app/src/main/java/com/kondenko/funshop/presentation/flux/State.kt) — это все возможные состояния приложения. Потомки [классов](https://github.com/Kondenko/FunBoxTestTask/blob/master/app/src/main/java/com/kondenko/funshop/presentation/flux/Action.kt) Action.Buyer и Action.Admin позволяют перейти от одного состояния к другому. 

Благодаря ViewModel можно не переживать о сохранении состояния любой сложности при перевороте экрана. 

## Как устроено приложение

В FunShop два экрана с похожими функциями — "показать все товары" и "изменить товар". Они оба управляются классом [GoodsViewModelImpl](https://github.com/Kondenko/FunBoxTestTask/blob/master/app/src/main/java/com/kondenko/funshop/presentation/viewmodel/GoodsViewModelImpl.kt), чтобы не дублировать код и избежать проблем с синхронизацией состояния. Каждый экран реагирует только на те состояния, которые для него актуальны.

Во фрагментах [FragmentStore](https://github.com/Kondenko/FunBoxTestTask/blob/master/app/src/main/java/com/kondenko/funshop/presentation/store/FragmentStore.kt) и [FragmentBackend](https://github.com/Kondenko/FunBoxTestTask/blob/master/app/src/main/java/com/kondenko/funshop/presentation/backend/FragmentBackend.kt) я использую ViewModel через соответствующие интерфейсы. Это позволяет разграничить действия, которые можно сделать на каждом экране. 

Библиотека ViewPager2 тоже помогла сократить дублирование кода — один и тот же адаптер подходит и для RecyclerView, и для ViewPager. 

## Сохранение и загрузка данных

Для хранения я использую Room. Когда база данных создаётся впервые, в нее [попадают](https://github.com/Kondenko/FunBoxTestTask/blob/master/app/src/main/java/com/kondenko/funshop/presentation/store/FragmentStore.kt#L56) данные из файла data.csv.

Класс [GoodsRepository](https://github.com/Kondenko/FunBoxTestTask/blob/master/app/src/main/java/com/kondenko/funshop/data/GoodsRepository.kt) служит адаптером для источника данных. В нашем случае их два — база данных и csv-файл. Они оба имплементируют интерфейс [GoodsProvider](https://github.com/Kondenko/FunBoxTestTask/blob/master/app/src/main/java/com/kondenko/funshop/data/GoodsProvider.kt) и позволяют независимо от происхождения товаров отдавать их в понятном приложению формате (observable со списком товаров).

## Покупка и сохранение товаров

Для работы с товарами я использую юзкейсы. [Классы](https://github.com/Kondenko/FunBoxTestTask/blob/master/app/src/main/java/com/kondenko/funshop/domain/UseCase.kt) UseCase.Subscribe и UseCase.Do отвечают за выполнение кода в отдельных потоках, а их потомки — за логику приложения. 

Если товар был изменен, пока его редактировали, на [экране редактирования](https://github.com/Kondenko/FunBoxTestTask/blob/master/app/src/main/java/com/kondenko/funshop/presentation/backend/FragmentItemEditor.kt) можно заменить значения полей на новые или оставить всё, как есть. 

В классе FragmentStore товары с количеством ≤ 0 [фильтруются](https://github.com/Kondenko/FunBoxTestTask/blob/master/app/src/main/java/com/kondenko/funshop/presentation/store/FragmentStore.kt#L56), чтобы покупатели не видели их в списке. В идеале фильтровать данные нужно в юзкейсе. В этом приложении я решил пожертвовать идеальностью архитектуры, чтобы не увеличивать количество классов.
