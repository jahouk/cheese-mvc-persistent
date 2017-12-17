package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping("cheese")
public class CheeseController {

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private CategoryDao categoryDao;

    // Request path: /cheese
    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("categories", categoryDao.findAll());
        model.addAttribute("title", "My Cheeses");

        return "cheese/index";
    }

    @RequestMapping(value = "category/{catId}", method = RequestMethod.GET)
    public String displayCheeseByCategory(Model model, @PathVariable int catId){
        Category cat = categoryDao.findOne(catId);
        List<Cheese> cheeses = cat.getCheeses();
        model.addAttribute("cheeses", cheeses);
        model.addAttribute("title", cat.getName());

        return "cheese/index";
    }


    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddCheeseForm(Model model) {
        model.addAttribute("title", "Add Cheese");
        model.addAttribute(new Cheese());
        model.addAttribute("categories", categoryDao.findAll());
        return "cheese/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddCheeseForm(
            @ModelAttribute  @Valid Cheese newCheese,
            Errors errors,
            @RequestParam int categoryId,
            Model model){

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Cheese");
            return "cheese/add";
        }

        Category cat = categoryDao.findOne(categoryId);
        newCheese.setCategory(cat);

        cheeseDao.save(newCheese);
        return "redirect:";
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String displayRemoveCheeseForm(Model model) {
        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "Remove Cheese");
        return "cheese/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveCheeseForm(@RequestParam int[] cheeseIds) {

        for (int cheeseId : cheeseIds) {
            cheeseDao.delete(cheeseId);
        }

        return "redirect:";
    }

    @RequestMapping(value = "edit/{cheeseId}", method = RequestMethod.GET)
    public String displayEditCheeseForm(Model model,  @PathVariable int cheeseId) {
        Cheese editCheese = cheeseDao.findOne(cheeseId);
        model.addAttribute("title", "Edit Cheese: " + editCheese.getName());
        model.addAttribute(editCheese);
        model.addAttribute("categories", categoryDao.findAll());
        model.addAttribute("edit", true);
        return "cheese/add";
    }

    @RequestMapping(value = "edit/{cheeseId}", method = RequestMethod.POST)
    public String processEditCheeseForm(
            @ModelAttribute  @Valid Cheese editCheese,
            Errors errors,
            @PathVariable int cheeseId,
            @RequestParam int categoryId,
            Model model){


        if (errors.hasErrors()) {
            model.addAttribute("title", "Edit Cheese: " + editCheese.getName());
            model.addAttribute("edit", true);
            return "cheese/add";
        }

        Cheese updateCheese = cheeseDao.findOne(cheeseId);

        updateCheese.setName(editCheese.getName());
        updateCheese.setDescription(editCheese.getDescription());
        updateCheese.setCategory(categoryDao.findOne(categoryId));

        cheeseDao.save(updateCheese);
        return "redirect:/cheese";
    }

}
