package com.mdr.food.presentation

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.chip.Chip
import com.mdr.food.R
import com.mdr.food.databinding.ActivityMainBinding
import com.mdr.food.databinding.ItemBannerBinding
import com.mdr.food.databinding.ItemCategoryBinding
import com.mdr.food.databinding.ItemFoodBinding
import com.mdr.food.domain.Food
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::bind)
    private val viewModel: MainViewModel by inject()
    private val banners by lazy {
        listOf(
            AppCompatResources.getDrawable(this, R.drawable.banner_1),
            AppCompatResources.getDrawable(this, R.drawable.banner_1),
            AppCompatResources.getDrawable(this, R.drawable.banner_1)
        )
    }
    private lateinit var categoriesItemAdapter: ItemAdapter<CategoryItem>
    private lateinit var foodItemAdapter: ItemAdapter<FoodItem>
    private lateinit var bannersItemAdapter: ItemAdapter<BannerItem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
        observeViewModel()
    }

    private fun initUI() {
        binding.rvFood.apply {
            foodItemAdapter = ItemAdapter<FoodItem>()
            val fastAdapter = FastAdapter.with(foodItemAdapter)

            val foodDecoration =
                DividerItemDecoration(
                    this@MainActivity,
                    DividerItemDecoration.VERTICAL
                )
            foodDecoration.setDrawable(
                AppCompatResources.getDrawable(
                    this@MainActivity,
                    R.drawable.divider_horizontal_line
                )!!
            )
            binding.rvFood.layoutManager = LinearLayoutManager(this@MainActivity)
            binding.rvFood.adapter = fastAdapter
            binding.rvFood.addItemDecoration(foodDecoration)
        }

        binding.rvCategories.apply {
            categoriesItemAdapter = ItemAdapter()
            val fastAdapter = FastAdapter.with(categoriesItemAdapter)
            val itemDecoration =
                DividerItemDecoration(
                    this@MainActivity,
                    DividerItemDecoration.HORIZONTAL
                )
            itemDecoration.setDrawable(
                AppCompatResources.getDrawable(
                    this@MainActivity,
                    R.drawable.divider_8
                )!!
            )
            layoutManager = LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = fastAdapter
            addItemDecoration(itemDecoration)
        }

        binding.rvBanners.apply {
            bannersItemAdapter = ItemAdapter<BannerItem>()
            val bannersFastAdapter = FastAdapter.with(bannersItemAdapter)
            val itemDecoration =
                DividerItemDecoration(
                    this@MainActivity,
                    DividerItemDecoration.HORIZONTAL
                )
            itemDecoration.setDrawable(
                AppCompatResources.getDrawable(
                    this@MainActivity,
                    R.drawable.divider_16
                )!!
            )
            layoutManager =
                LinearLayoutManager(
                    this@MainActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = bannersFastAdapter
            bannersItemAdapter.add(banners.map { BannerItem(it) })
            addItemDecoration(itemDecoration)
        }
    }

    private fun observeViewModel() {
        viewModel.foodFlow.onEach {
            if (it == null) return@onEach
            foodItemAdapter.add(it.map { FoodItem(it) })
        }.launchIn(lifecycleScope)

        viewModel.categoriesFlow.onEach { categoriesModel ->
            if (categoriesModel == null) return@onEach
            categoriesItemAdapter.add(categoriesModel.categories.mapIndexed { index, category ->
                CategoryItem(category, index == categoriesModel.selected)
            })
        }.launchIn(lifecycleScope)
    }

}

class FoodItem(private val food: Food) : AbstractBindingItem<ItemFoodBinding>() {
    override val type: Int
        get() = R.id.fastadapter_food_item_id

    override fun bindView(binding: ItemFoodBinding, payloads: List<Any>) {
        loadImage(binding.iv, food.imageUrl)
        binding.tvTitle.text = food.title
        binding.tvDesc.text = food.description
        binding.btnAdd.text = "От ${food.minPrice} р"
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemFoodBinding = ItemFoodBinding.inflate(inflater, parent, false)
}

class BannerItem(private val banner: Drawable?) :
    AbstractBindingItem<ItemBannerBinding>() {
    override val type: Int
        get() = R.id.fastadapter_banner_item_id

    override fun bindView(binding: ItemBannerBinding, payloads: List<Any>) {
        binding.ivBanner.setImageDrawable(banner)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemBannerBinding = ItemBannerBinding.inflate(inflater, parent, false)
}

class CategoryItem(private val category: String, private val selected: Boolean) :
    AbstractBindingItem<ItemCategoryBinding>() {

    override val type: Int
        get() = R.id.fastadapter_category_item_id

    override fun bindView(binding: ItemCategoryBinding, payloads: List<Any>) {
        binding.chipSelected.isVisible = false
        binding.chip.isVisible = false
        if (selected) {
            binding.chipSelected.isVisible = true
            binding.chipSelected.text = category
        } else {
            binding.chip.isVisible = true
            binding.chip.text = category
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemCategoryBinding = ItemCategoryBinding.inflate(inflater, parent, false)
}