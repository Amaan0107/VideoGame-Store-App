package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories()
    {
       String sql = "SELECT * " +
               "FROM Category " +
               "ORDER BY category_id;";

       List<Category> categories = new ArrayList<>();

       try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet row = statement.executeQuery()){
           while (row.next()){
               categories.add(mapRow(row));
           }
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
       return categories;
    }

    @Override
    public Category getById(int categoryId)
    {
        String sql = "SELECT * " +
               "FROM Category " +
               "WHERE category_id = ?";

        try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, categoryId);

            try (ResultSet row = statement.executeQuery()){
                if (row.next())
                {
                    return mapRow(row);
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Category create(Category category)
    {

    }

    @Override
    public void update(int categoryId, Category category)
    {
        // update category
    }

    @Override
    public void delete(int categoryId)
    {
        // delete category
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
